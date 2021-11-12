package com.szusta.meduva.service.freetimescanner;

import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.User;
import com.szusta.meduva.model.WorkHours;
import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.payload.Term;
import com.szusta.meduva.payload.TimeRange;
import com.szusta.meduva.repository.WorkHoursRepository;
import com.szusta.meduva.repository.equipment.EquipmentItemRepository;
import com.szusta.meduva.service.ScheduleChecker;
import com.szusta.meduva.util.TimeUtils;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Make sure that before using FreeTimeScanner, its fields are set appropriately
 */
@Component
@Scope("prototype")
@Setter
public class FreeTimeScanner {

    public static int TIME_STEP_IN_MINUTES = 15;

    private User worker;
    private Service service;
    private List<Room> rooms;

    private ScheduleChecker scheduleChecker;
    private WorkHoursRepository workHoursRepository;
    private EquipmentItemRepository equipmentItemRepository;

    private Room availableRoom;
    private EquipmentItem availableEqItem;
    private Calendar potentialTermStart;
    private Calendar potentialTermEnd;

    @Autowired
    public FreeTimeScanner(ScheduleChecker scheduleChecker,
                           WorkHoursRepository workHoursRepository,
                           EquipmentItemRepository equipmentItemRepository) {
        this.scheduleChecker = scheduleChecker;
        this.workHoursRepository = workHoursRepository;
        this.equipmentItemRepository = equipmentItemRepository;
    }

    private void checkIfWorkerIsSet() {
        if (worker == null) {
            throw new RuntimeException("FreeTimeScanner not set appropriately: worker is not set");
        }
    }

    private void checkIfServiceIsSet() {
        if (service == null) {
            throw new RuntimeException("FreeTimeScanner not set appropriately: service is not set");
        }
    }

    private void checkIfRoomsAreSet() {
        if (rooms == null) {
            throw new RuntimeException("FreeTimeScanner not set appropriately: rooms are not set");
        }
    }

    public boolean isWorkerDayAvailable(Calendar dayStart) {
        checkIfWorkerIsSet();
        checkIfServiceIsSet();
        checkIfRoomsAreSet();
        try {
            return hasFreeTerm(dayStart);
        } catch (NotAvailableException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    private boolean hasFreeTerm(Calendar dayStart) throws NotAvailableException {
        this.potentialTermStart = getWorkStartTime(dayStart);
        this.potentialTermEnd = getPotentialTermEnd(potentialTermStart, service.getDurationInMin());
        Date workEndTime = getWorkEndTime(dayStart);
        do {
            TimeRange potentialTermTimeRange = new TimeRange(potentialTermStart.getTime(), potentialTermEnd.getTime());

            if (scheduleChecker.isWorkerFree(potentialTermTimeRange, worker)) {
                return doesFreeRoomExist(potentialTermTimeRange);
            }

            this.potentialTermStart.add(Calendar.MINUTE, TIME_STEP_IN_MINUTES);
            this.potentialTermEnd = getPotentialTermEnd(this.potentialTermStart, service.getDurationInMin());
        } while (this.potentialTermEnd.before(TimeUtils.getCalendar(workEndTime)));

        return false;
    }

    private Calendar getWorkStartTime(Calendar day) throws NotAvailableException {
        WorkHours workHours = getWorkHours(day);
        return TimeUtils.getCalendar(workHours.getStartTime());
    }

    private Date getWorkEndTime(Calendar day) throws NotAvailableException {
        WorkHours workHours = getWorkHours(day);
        Calendar cal = Calendar.getInstance();
        cal.setTime(workHours.getEndTime());
        cal.add(Calendar.MINUTE, 1); // because service could end exactly at work end time
        return cal.getTime();
    }

    private WorkHours getWorkHours(Calendar day) throws NotAvailableException {
        Date dayStart = TimeUtils.getDayStart(day.getTime());
        Date dayEnd = TimeUtils.getDayEnd(dayStart);

        List<WorkHours> dayWorkHours =
                workHoursRepository.getAllByWorkerIdBetween(worker.getId(), dayStart, dayEnd);
        if (dayWorkHours.isEmpty())
            throw new NotAvailableException("Worker doesn't have work hours that day");

        // dayWorkHours is singleton - in one day, there has to be only one work_hours record for a worker
        return dayWorkHours.get(0);
    }

    private Calendar getPotentialTermEnd(Calendar potentialTermStart, int serviceDurationInMinutes) {
        Calendar potentialTermEnd = Calendar.getInstance();
        potentialTermEnd.setTime(potentialTermStart.getTime());
        potentialTermEnd.add(Calendar.MINUTE, serviceDurationInMinutes);
        return potentialTermEnd;
    }

    private boolean doesFreeRoomExist(TimeRange potentialTermTimeRange) {
        Room availableRoom = getFirstAvailableRoom(rooms, potentialTermTimeRange);
        if (availableRoom != null) {
            if (service.isItemless()) {
                return true;
            } else {
                return doesFreeItemExist(availableRoom, potentialTermTimeRange);
            }
        } else {
            return false;
        }
    }

    private Room getFirstAvailableRoom(List<Room> suitableRooms, TimeRange timeRange) {
        for (Room room : suitableRooms) {
            if (scheduleChecker.isRoomFree(timeRange, room)) {
                return room;
            }
        }
        return null;
    }

    private boolean doesFreeItemExist(Room availableRoom, TimeRange potentialTermTimeRange) {
        List<EquipmentItem> suitableEqItems =
                equipmentItemRepository.findAllSuitableForServiceInRoom(service.getId(), availableRoom.getId());
        EquipmentItem availableEqItem = getFirstAvailableEqItem(suitableEqItems, potentialTermTimeRange);
        return availableEqItem != null;
    }

    private EquipmentItem getFirstAvailableEqItem(List<EquipmentItem> suitableEqItems, TimeRange timeRange) {
        for (EquipmentItem item : suitableEqItems) {
            if (scheduleChecker.isEqItemFree(timeRange, item)) {
                System.out.println(item.getName() + "is free between " + timeRange.getStartTime() + " and " + timeRange.getEndTime());
                return item;
            }
        }
        return null;
    }

    public List<Term> getWorkerPossibleTerms(Date day) throws NotAvailableException {
        List<Term> possibleTerms = new ArrayList<>();

        this.potentialTermStart = getWorkStartTime(TimeUtils.getCalendar(day));
        this.potentialTermEnd = getPotentialTermEnd(this.potentialTermStart,
                service.getDurationInMin());
        Date workEndTime = getWorkEndTime(TimeUtils.getCalendar(day));
        do {
            TimeRange potentialTermTimeRange = new TimeRange(
                    this.potentialTermStart.getTime(),
                    this.potentialTermEnd.getTime());

            if (scheduleChecker.isWorkerFree(potentialTermTimeRange, worker)) {
                possibleTerms = checkResources(possibleTerms, potentialTermTimeRange);
            }

            this.potentialTermStart.add(Calendar.MINUTE, TIME_STEP_IN_MINUTES);
            this.potentialTermEnd = getPotentialTermEnd(this.potentialTermStart,
                    service.getDurationInMin());
        } while (this.potentialTermEnd.before(TimeUtils.getCalendar(workEndTime)));

        return possibleTerms;
    }

    private List<Term> checkResources(List<Term> possibleTerms, TimeRange potentialTermTimeRange) {
        this.availableRoom = getFirstAvailableRoom(rooms, potentialTermTimeRange);
        if (this.availableRoom != null) {
            if (service.isItemless()) {
                possibleTerms.add(createTermWithoutItem());
            } else {
                List<EquipmentItem> suitableEqItems =
                        equipmentItemRepository.findAllSuitableForServiceInRoom(
                                service.getId(),
                                availableRoom.getId());
                this.availableEqItem =
                        getFirstAvailableEqItem(suitableEqItems, potentialTermTimeRange);
                if (this.availableEqItem != null) {
                    possibleTerms.add(createTermWithItem());
                }
            }
        }
        return possibleTerms;
    }

    private Term createTermWithItem() {
        Term term = new Term();
        term.setWorkerId(worker.getId());
        term.setServiceId(service.getId());
        term.setStartTime(potentialTermStart.getTime());
        term.setEndTime(this.potentialTermEnd.getTime());
        term.setRoomId(availableRoom.getId());
        term.setEqItemId(availableEqItem.getId());
        return term;
    }

    private Term createTermWithoutItem() {
        Term term = new Term();
        term.setWorkerId(worker.getId());
        term.setServiceId(service.getId());
        term.setStartTime(this.potentialTermStart.getTime());
        term.setEndTime(this.potentialTermEnd.getTime());
        term.setRoomId(this.availableRoom.getId());
        return term;
    }
}
