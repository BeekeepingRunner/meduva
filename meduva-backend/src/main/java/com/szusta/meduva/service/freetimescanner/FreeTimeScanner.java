package com.szusta.meduva.service.freetimescanner;

import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.User;
import com.szusta.meduva.model.WorkHours;
import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.payload.TimeRange;
import com.szusta.meduva.repository.WorkHoursRepository;
import com.szusta.meduva.repository.equipment.EquipmentItemRepository;
import com.szusta.meduva.service.ScheduleChecker;
import com.szusta.meduva.util.TimeUtils;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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

    private User worker;
    private Service service;
    private List<Room> rooms;

    private ScheduleChecker scheduleChecker;
    private WorkHoursRepository workHoursRepository;

    private EquipmentItemRepository equipmentItemRepository;

    @Autowired
    public FreeTimeScanner(ScheduleChecker scheduleChecker,
                           WorkHoursRepository workHoursRepository,
                           EquipmentItemRepository equipmentItemRepository) {
        this.scheduleChecker = scheduleChecker;
        this.workHoursRepository = workHoursRepository;
        this.equipmentItemRepository = equipmentItemRepository;
    }

    private void checkInitialSettings() {
        if (worker == null || service == null || rooms == null) {
            throw new RuntimeException("FreeTimeScanner not set appropriately, there are null fields");
        }
    }

    public boolean isWorkerDayAvailable(Calendar dayStart) {
        checkInitialSettings();
        try {
            return hasFreeTerm(dayStart);
        } catch (NotAvailableException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    private boolean hasFreeTerm(Calendar dayStart) throws NotAvailableException {
        Calendar potentialTermStart = getWorkStartTime(dayStart);

        Date potentialTermEnd = getPotentialTermEnd(potentialTermStart.getTime(), service.getDurationInMin());
        Date workEndTime = getWorkEndTime(dayStart);
        do {
            TimeRange potentialTermTimeRange = new TimeRange(potentialTermStart.getTime(), potentialTermEnd);

            if (scheduleChecker.isWorkerFree(potentialTermTimeRange, worker)) {
                Room availableRoom = getFirstAvailableRoom(rooms, potentialTermTimeRange);
                if (!service.isItemless()) {
                    return doesFreeItemExist(availableRoom, potentialTermTimeRange);
                }
            }

            potentialTermStart.add(Calendar.MINUTE, 30);
            potentialTermEnd = getPotentialTermEnd(potentialTermEnd, service.getDurationInMin());
        } while (potentialTermEnd.before(workEndTime));

        return false;
    }

    private Calendar getWorkStartTime(Calendar dayStartCal) throws NotAvailableException {
        WorkHours workHours = getWorkHours(dayStartCal);
        return TimeUtils.getCalendar(workHours.getStartTime());
    }

    private Date getWorkEndTime(Calendar dayStartCal) throws NotAvailableException {
        WorkHours workHours = getWorkHours(dayStartCal);
        Calendar cal = Calendar.getInstance();
        cal.setTime(workHours.getEndTime());
        cal.add(Calendar.MINUTE, 1); // because service could end exactly at work end time
        return cal.getTime();
    }

    private WorkHours getWorkHours(Calendar dayStartCal) throws NotAvailableException {
        Date dayStart = dayStartCal.getTime();
        Date dayEnd = TimeUtils.getDayEnd(dayStart);

        List<WorkHours> dayWorkHours =
                workHoursRepository.getAllByWorkerIdBetween(worker.getId(), dayStart, dayEnd);
        if (dayWorkHours.isEmpty())
            throw new NotAvailableException("Worker doesn't have work hours that day");

        // dayWorkHours is singleton - in one day, there has to be only one work_hours record for a worker
        return dayWorkHours.get(0);
    }

    private Date getPotentialTermEnd(Date potentialTermStart, int serviceDurationInMinutes) {
        Calendar temp = Calendar.getInstance();
        temp.setTime(potentialTermStart);
        temp.add(Calendar.MINUTE, serviceDurationInMinutes);
        return temp.getTime();
    }

    private Room getFirstAvailableRoom(List<Room> suitableRooms, TimeRange timeRange)
            throws NotAvailableException {
        for (Room room : suitableRooms) {
            if (scheduleChecker.isRoomFree(timeRange, room)) {
                return room;
            }
        }
        throw new NotAvailableException("There are no available rooms");
    }

    private boolean doesFreeItemExist(Room availableRoom, TimeRange potentialTermTimeRange) throws NotAvailableException {
        List<EquipmentItem> suitableEqItems =
                equipmentItemRepository.findAllSuitableForServiceInRoom(service.getId(), availableRoom.getId());
        return getFirstAvailableEqItem(suitableEqItems, potentialTermTimeRange) != null;
    }

    private EquipmentItem getFirstAvailableEqItem(List<EquipmentItem> suitableEqItems, TimeRange timeRange)
            throws NotAvailableException {
        for (EquipmentItem item : suitableEqItems) {
            if (scheduleChecker.isEqItemFree(timeRange, item)) {
                return item;
            }
        }
        throw new NotAvailableException("There are no available equipment items");
    }
}
