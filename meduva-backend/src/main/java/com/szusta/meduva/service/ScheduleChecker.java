package com.szusta.meduva.service;

import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.User;
import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.model.schedule.EquipmentSchedule;
import com.szusta.meduva.model.schedule.status.enums.EEquipmentStatus;
import com.szusta.meduva.payload.TimeRange;
import com.szusta.meduva.repository.equipment.EquipmentItemRepository;
import com.szusta.meduva.repository.equipment.EquipmentModelRepository;
import com.szusta.meduva.repository.schedule.equipment.EquipmentScheduleRepository;
import com.szusta.meduva.repository.schedule.room.RoomScheduleRepository;
import com.szusta.meduva.repository.schedule.worker.WorkerScheduleRepository;
import com.szusta.meduva.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@org.springframework.stereotype.Service
public class ScheduleChecker {

    private EquipmentItemRepository equipmentItemRepository;
    private EquipmentModelRepository equipmentModelRepository;
    private UserService userService;

    private EquipmentScheduleRepository equipmentScheduleRepository;
    private RoomScheduleRepository roomScheduleRepository;
    private WorkerScheduleRepository workerScheduleRepository;

    @Autowired
    public ScheduleChecker(EquipmentItemRepository equipmentItemRepository,
                           EquipmentModelRepository equipmentModelRepository,
                           UserService userService,
                           EquipmentScheduleRepository equipmentScheduleRepository,
                           RoomScheduleRepository roomScheduleRepository,
                           WorkerScheduleRepository workerScheduleRepository) {
        this.equipmentItemRepository = equipmentItemRepository;
        this.equipmentModelRepository = equipmentModelRepository;
        this.userService = userService;
        this.equipmentScheduleRepository = equipmentScheduleRepository;
        this.roomScheduleRepository = roomScheduleRepository;
        this.workerScheduleRepository = workerScheduleRepository;
    }
    // TODO: refactor - code repetition
    //

    /*
    private Optional<Room> getFirstAvailableRoom(List<Room> suitableRooms, Date currentCheckStart, Date currentCheckEnd) {
        for (Room room : suitableRooms) {
            if (isRoomFree(currentCheckStart, currentCheckEnd, room)) {
                return Optional.of(room);
            }
        }
        return Optional.empty();
    }

    private Optional<EquipmentItem> getFirstAvailableEqItem(List<EquipmentItem> suitableEqItems, Date currentCheckStart, Date currentCheckEnd) {
        for (EquipmentItem item : suitableEqItems) {
            if (isEqItemFree(currentCheckStart, currentCheckEnd, item)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }
     */

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
        Date startTime = timeRange.getStartTime();
        Date endTime = timeRange.getEndTime();
        return equipmentScheduleRepository
                .findAnyBetween(startTime, endTime, eqItem.getId())
                .isEmpty();
    }

    public List<EquipmentSchedule> getItemUnavailabilityIn(EquipmentItem item, TimeRange weekBoundaries) {
        Long eqStatus = EEquipmentStatus.EQUIPMENT_UNAVAILABLE.getValue();
        return equipmentScheduleRepository.findAllBetween(
                weekBoundaries.getStartTime(),
                weekBoundaries.getEndTime(),
                item.getId(),
                eqStatus);
    }
}
