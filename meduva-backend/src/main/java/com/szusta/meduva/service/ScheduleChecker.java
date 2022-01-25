package com.szusta.meduva.service;

import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.User;
import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.model.schedule.EquipmentSchedule;
import com.szusta.meduva.model.schedule.RoomSchedule;
import com.szusta.meduva.model.schedule.status.enums.EEquipmentStatus;
import com.szusta.meduva.model.schedule.status.enums.ERoomStatus;
import com.szusta.meduva.model.schedule.status.enums.EWorkerStatus;
import com.szusta.meduva.payload.TimeRange;
import com.szusta.meduva.repository.schedule.equipment.EquipmentScheduleRepository;
import com.szusta.meduva.repository.schedule.room.RoomScheduleRepository;
import com.szusta.meduva.repository.schedule.visit.VisitRepository;
import com.szusta.meduva.repository.schedule.worker.WorkerScheduleRepository;
import com.szusta.meduva.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@org.springframework.stereotype.Service
public class ScheduleChecker {

    private EquipmentScheduleRepository equipmentScheduleRepository;
    private RoomScheduleRepository roomScheduleRepository;
    private WorkerScheduleRepository workerScheduleRepository;
    private VisitRepository visitRepository;

    @Autowired
    public ScheduleChecker(EquipmentScheduleRepository equipmentScheduleRepository,
                           RoomScheduleRepository roomScheduleRepository,
                           WorkerScheduleRepository workerScheduleRepository,
                           VisitRepository visitRepository) {
        this.equipmentScheduleRepository = equipmentScheduleRepository;
        this.roomScheduleRepository = roomScheduleRepository;
        this.workerScheduleRepository = workerScheduleRepository;
        this.visitRepository = visitRepository;
    }

    public boolean isWorkerFree(TimeRange timeRange, User worker) {
        Date startTime = timeRange.getStartTime();
        Date endTime = timeRange.getEndTime();
        return workerScheduleRepository
                .findAllDuring(startTime, endTime, worker.getId())
                .isEmpty();
    }

    public boolean isRoomFree(TimeRange timeRange, Room room) {
        Date startTime = timeRange.getStartTime();
        Date endTime = timeRange.getEndTime();
        return roomScheduleRepository
                .findAllDuring(startTime, endTime, room.getId())
                .isEmpty();
    }

    public boolean isEqItemFree(TimeRange timeRange, EquipmentItem eqItem) {
        return equipmentScheduleRepository
                .findAllDuring(
                        timeRange.getStartTime(),
                        timeRange.getEndTime(),
                        eqItem.getId())
                .isEmpty();
    }

    public boolean isBusyWith(User worker, EWorkerStatus workerStatus, TimeRange timeRange) {
        return !workerScheduleRepository
                .findAllDuring(
                        timeRange.getStartTime(),
                        timeRange.getEndTime(),
                        worker.getId(),
                        workerStatus.getValue())
                .isEmpty();
    }

    public boolean isBusyWith(Room room, ERoomStatus roomStatus, TimeRange timeRange) {
        return !roomScheduleRepository
                .findAllDuring(
                        timeRange.getStartTime(),
                        timeRange.getEndTime(),
                        room.getId(),
                        roomStatus.getValue())
                .isEmpty();
    }

    public boolean isBusyWith(EquipmentItem eqItem, EEquipmentStatus equipmentStatus, TimeRange timeRange) {
        return !equipmentScheduleRepository
                .findAllDuring(
                        timeRange.getStartTime(),
                        timeRange.getEndTime(),
                        eqItem.getId(),
                        equipmentStatus.getValue())
                .isEmpty();
    }

    public List<EquipmentSchedule> getItemUnavailabilityIn(EquipmentItem item, TimeRange weekBoundaries) {
        return equipmentScheduleRepository.findAllBetween(
                TimeUtils.getDayStart(weekBoundaries.getStartTime()),
                TimeUtils.getDayEnd(weekBoundaries.getEndTime()),
                item.getId(),
                EEquipmentStatus.EQUIPMENT_UNAVAILABLE.getValue());
    }

    public List<RoomSchedule> getRoomUnavailabilityIn(Room room, TimeRange weekBoundaries) {
        return roomScheduleRepository.findAllDuring(
                TimeUtils.getDayStart(weekBoundaries.getStartTime()),
                TimeUtils.getDayEnd(weekBoundaries.getEndTime()),
                room.getId(),
                ERoomStatus.ROOM_UNAVAILABLE.getValue());
    }

    public boolean isUsedInTheFuture(EquipmentItem eqItem) {
        return !equipmentScheduleRepository.findAllInTheFuture(
                eqItem.getId(), EEquipmentStatus.EQUIPMENT_OCCUPIED.getValue())
                .isEmpty();
    }

    public boolean isUsedInTheFuture(Service service) {
        return visitRepository.existsInTheFutureWith(service.getId());
    }
}
