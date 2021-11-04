package com.szusta.meduva.service;

import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.User;
import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.model.schedule.EquipmentSchedule;
import com.szusta.meduva.model.schedule.RoomSchedule;
import com.szusta.meduva.model.schedule.status.enums.EEquipmentStatus;
import com.szusta.meduva.model.schedule.status.enums.ERoomStatus;
import com.szusta.meduva.payload.TimeRange;
import com.szusta.meduva.repository.WorkHoursRepository;
import com.szusta.meduva.repository.schedule.equipment.EquipmentScheduleRepository;
import com.szusta.meduva.repository.schedule.room.RoomScheduleRepository;
import com.szusta.meduva.repository.schedule.worker.WorkerScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@org.springframework.stereotype.Service
public class ScheduleChecker {

    private EquipmentScheduleRepository equipmentScheduleRepository;
    private RoomScheduleRepository roomScheduleRepository;
    private WorkerScheduleRepository workerScheduleRepository;
    private WorkHoursRepository workHoursRepository;

    @Autowired
    public ScheduleChecker(EquipmentScheduleRepository equipmentScheduleRepository,
                           RoomScheduleRepository roomScheduleRepository,
                           WorkerScheduleRepository workerScheduleRepository,
                           WorkHoursRepository workHoursRepository) {
        this.equipmentScheduleRepository = equipmentScheduleRepository;
        this.roomScheduleRepository = roomScheduleRepository;
        this.workerScheduleRepository = workerScheduleRepository;
        this.workHoursRepository = workHoursRepository;
    }

    public boolean isWorkerFree(TimeRange timeRange, User worker) {
        Date startTime = timeRange.getStartTime();
        Date endTime = timeRange.getEndTime();
        return workerScheduleRepository
                .findAnyBetween(startTime, endTime, worker.getId())
                .isEmpty();
    }

    public boolean isRoomFree(TimeRange timeRange, Room room) {
        Date startTime = timeRange.getStartTime();
        Date endTime = timeRange.getEndTime();
        return roomScheduleRepository
                .findAnyBetween(startTime, endTime, room.getId())
                .isEmpty();
    }

    public boolean isEqItemFree(TimeRange timeRange, EquipmentItem eqItem) {
        return equipmentScheduleRepository
                .findAllBetween(
                        timeRange.getStartTime(),
                        timeRange.getEndTime(),
                        eqItem.getId(),
                        EEquipmentStatus.EQUIPMENT_OCCUPIED.getValue())
                .isEmpty();
    }

    public List<EquipmentSchedule> getItemUnavailabilityIn(EquipmentItem item, TimeRange weekBoundaries) {
        return equipmentScheduleRepository.findAllBetween(
                weekBoundaries.getStartTime(),
                weekBoundaries.getEndTime(),
                item.getId(),
                EEquipmentStatus.EQUIPMENT_UNAVAILABLE.getValue());
    }

    public List<RoomSchedule> getRoomUnavailabilityIn(Room room, TimeRange weekBoundaries) {
        return roomScheduleRepository.findAllBetween(
                weekBoundaries.getStartTime(),
                weekBoundaries.getEndTime(),
                room.getId(),
                ERoomStatus.ROOM_UNAVAILABLE.getValue());
    }


    public List<Date> getAvailableDaysOfMonth(User worker, Service service, Date anyDayOfMonth) {

        List<Date> availableDaysOfMonth = new ArrayList<>();

        // TODO: algorithmmmm

        return availableDaysOfMonth;
    }
}
