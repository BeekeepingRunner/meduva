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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Make sure that before using FreeTimeScanner, its fields are set appropriately
 */
@Component
@Scope("prototype")
@Setter
public class FreeTimeScanner {

    public static int TIME_STEP_IN_MINUTES = 15;

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

    private void checkIfContextIsComplete(VisitContext visitContext) {
        if (visitContext.getWorker() == null
                || visitContext.getService() == null
                || visitContext.getSuitableRooms() == null) {
            throw new RuntimeException("Visit context is not complete (worker, service, suitableRooms)");
        }
    }

    private void checkIfBasicContextExists(VisitContext visitContext) {
        if (visitContext.getWorker() == null
                || visitContext.getService() == null) {
            throw new RuntimeException("Basic visit context isn't set (worker, service)");
        }
    }

    public boolean isWorkerDayAvailable(Calendar dayStart, VisitContext visitContext) {
        checkIfContextIsComplete(visitContext);
        try {
            return hasFreeTerm(dayStart, visitContext);
        } catch (NotAvailableException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    private boolean hasFreeTerm(Calendar dayStart, VisitContext visitContext) throws NotAvailableException {
        this.potentialTermStart = getWorkStartTime(dayStart, visitContext.getWorker());
        this.potentialTermEnd = getPotentialTermEnd(potentialTermStart, visitContext.getService().getDurationInMin());
        Date workEndTime = getWorkEndTime(dayStart, visitContext.getWorker());
        do {
            TimeRange potentialTermTimeRange = new TimeRange(potentialTermStart.getTime(), potentialTermEnd.getTime());

            if (scheduleChecker.isWorkerFree(potentialTermTimeRange, visitContext.getWorker())) {
                return doesFreeRoomExist(potentialTermTimeRange, visitContext.getSuitableRooms(), visitContext.getService());
            }

            this.potentialTermStart.add(Calendar.MINUTE, TIME_STEP_IN_MINUTES);
            this.potentialTermEnd = getPotentialTermEnd(this.potentialTermStart, visitContext.getService().getDurationInMin());
        } while (this.potentialTermEnd.before(TimeUtils.getCalendar(workEndTime)));

        return false;
    }

    private Calendar getWorkStartTime(Calendar day, User worker) throws NotAvailableException {
        WorkHours workHours = getWorkHours(day, worker);
        return TimeUtils.getCalendar(workHours.getStartTime());
    }

    private Date getWorkEndTime(Calendar day, User worker) throws NotAvailableException {
        WorkHours workHours = getWorkHours(day, worker);
        Calendar cal = Calendar.getInstance();
        cal.setTime(workHours.getEndTime());
        cal.add(Calendar.MINUTE, 1); // because service could end exactly at work end time
        return cal.getTime();
    }

    private WorkHours getWorkHours(Calendar day, User worker) throws NotAvailableException {
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

    private boolean doesFreeRoomExist(TimeRange potentialTermTimeRange, List<Room> suitableRooms, Service service) {
        Room availableRoom = getFirstAvailableRoom(suitableRooms, potentialTermTimeRange);
        if (availableRoom != null) {
            if (service.isItemless()) {
                return true;
            } else {
                return doesFreeItemExist(potentialTermTimeRange, availableRoom, service);
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

    private boolean doesFreeItemExist(TimeRange potentialTermTimeRange, Room availableRoom, Service service) {
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

    public List<Term> getWorkerPossibleTerms(Date day, VisitContext visitContext) throws NotAvailableException {
        checkIfContextIsComplete(visitContext);
        List<Term> possibleTerms = new ArrayList<>();

        this.potentialTermStart = getWorkStartTime(TimeUtils.getCalendar(day), visitContext.getWorker());
        this.potentialTermEnd = getPotentialTermEnd(this.potentialTermStart,
                visitContext.getService().getDurationInMin());
        Date workEndTime = getWorkEndTime(TimeUtils.getCalendar(day), visitContext.getWorker());
        do {
            TimeRange potentialTermTimeRange = new TimeRange(
                    this.potentialTermStart.getTime(),
                    this.potentialTermEnd.getTime());

            if (scheduleChecker.isWorkerFree(potentialTermTimeRange, visitContext.getWorker())) {
                possibleTerms = checkResources(possibleTerms, potentialTermTimeRange, visitContext);
            }

            this.potentialTermStart.add(Calendar.MINUTE, TIME_STEP_IN_MINUTES);
            this.potentialTermEnd = getPotentialTermEnd(this.potentialTermStart,
                    visitContext.getService().getDurationInMin());
        } while (this.potentialTermEnd.before(TimeUtils.getCalendar(workEndTime)));

        return possibleTerms;
    }

    private List<Term> checkResources(List<Term> possibleTerms, TimeRange potentialTermTimeRange, VisitContext visitContext) {
        this.availableRoom = getFirstAvailableRoom(visitContext.getSuitableRooms(), potentialTermTimeRange);
        if (this.availableRoom != null) {
            if (visitContext.getService().isItemless()) {
                possibleTerms.add(createTermWithoutItem(visitContext));
            } else {
                List<EquipmentItem> suitableEqItems =
                        equipmentItemRepository.findAllSuitableForServiceInRoom(
                                visitContext.getService().getId(),
                                availableRoom.getId());
                this.availableEqItem =
                        getFirstAvailableEqItem(suitableEqItems, potentialTermTimeRange);
                if (this.availableEqItem != null) {
                    possibleTerms.add(createTermWithItem(visitContext));
                }
            }
        }
        return possibleTerms;
    }

    private Term createTermWithItem(VisitContext visitContext) {
        Term term = new Term();
        term.setWorkerId(visitContext.getWorker().getId());
        term.setServiceId(visitContext.getService().getId());
        term.setStartTime(potentialTermStart.getTime());
        term.setEndTime(this.potentialTermEnd.getTime());
        term.setRoomId(availableRoom.getId());
        term.setEqItemId(availableEqItem.getId());
        return term;
    }

    private Term createTermWithoutItem(VisitContext visitContext) {
        Term term = new Term();
        term.setWorkerId(visitContext.getWorker().getId());
        term.setServiceId(visitContext.getService().getId());
        term.setStartTime(this.potentialTermStart.getTime());
        term.setEndTime(this.potentialTermEnd.getTime());
        term.setRoomId(this.availableRoom.getId());
        return term;
    }
}
