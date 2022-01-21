package com.szusta.meduva.service.room;

import com.szusta.meduva.exception.AlreadyExistsException;
import com.szusta.meduva.exception.EntityRecordNotFoundException;
import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.model.schedule.EquipmentSchedule;
import com.szusta.meduva.model.schedule.RoomSchedule;
import com.szusta.meduva.model.schedule.Schedule;
import com.szusta.meduva.model.schedule.status.enums.ERoomStatus;
import com.szusta.meduva.payload.TimeRange;
import com.szusta.meduva.repository.RoomRepository;
import com.szusta.meduva.service.ScheduleChecker;
import com.szusta.meduva.service.equipment.ItemsDisconnector;
import com.szusta.meduva.util.TimeUtils;
import com.szusta.meduva.util.UndeletableWithNameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {

    private RoomRepository roomRepository;
    private ItemsDisconnector itemsDisconnector;
    private ScheduleChecker scheduleChecker;
    private RoomScheduleManager roomScheduleManager;

    @Autowired
    public RoomService(RoomRepository roomRepository,
                       ItemsDisconnector itemsDisconnector,
                       ScheduleChecker scheduleChecker,
                       RoomScheduleManager roomScheduleManager) {
        this.roomRepository = roomRepository;
        this.itemsDisconnector = itemsDisconnector;
        this.scheduleChecker = scheduleChecker;
        this.roomScheduleManager = roomScheduleManager;
    }

    public List<Room> findAllRooms() {
        return this.roomRepository.findAll();
    }

    public List<Room> findAllUndeletedRooms() {
        return this.roomRepository.findAllUndeleted();
    }

    public Room findById(Long id) {
        return this.roomRepository.findById(id)
                .orElseThrow(() -> new EntityRecordNotFoundException("Room not found with id : " + id));
    }

    public boolean doesRoomExistByName(String roomName) {
        return this.roomRepository.existsByName(roomName);
    }

    public Room saveNewRoom(Room room) {

        if (UndeletableWithNameUtils.canBeSaved(this.roomRepository, room.getName())) {
            return this.roomRepository.save(room);
        } else
            throw new AlreadyExistsException("Room already exists with name: " + room.getName());
    }

    public Room editRoomServices(Room room) {
        return this.roomRepository.save(room);
    }

    @Transactional
    public void markAsDeleted(Long id) {
        Room room = roomRepository.findById(id)
                        .orElseThrow(() -> new EntityRecordNotFoundException("Room not found with id : " + id));

        itemsDisconnector.disconnectItems(room);
        room.markAsDeleted();
        roomRepository.save(room);
    }

    public void deleteAllRooms(){
        List<Room> roomsToDelete = this.roomRepository.findAllUndeleted();
        for (Room room:roomsToDelete) {
            markAsDeleted(room.getId());
        }
    }

    public void deleteAllRoomsPermanently() {
        this.roomRepository.deleteAll();
    }

    public List<TimeRange> getRoomWeeklyUnavailability(Room room, TimeRange weekBoundaries) {
        List<RoomSchedule> weeklyUnavailability = scheduleChecker.getRoomUnavailabilityIn(room, weekBoundaries);
        return weeklyUnavailability.stream()
                .map(unavailableTimeRange ->
                        new TimeRange(unavailableTimeRange.getTimeFrom(), unavailableTimeRange.getTimeTo())
                ).collect(Collectors.toList());
    }

    @Transactional
    public RoomSchedule setRoomDayUnavailability(Long roomId, Date day) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityRecordNotFoundException("Cannot set room unavailability: room not found in DB with id : " + roomId));

        TimeRange allDay = new TimeRange(
                TimeUtils.getDayStart(day).getTime(),
                TimeUtils.getDayEnd(day).getTime());

        if (scheduleChecker.isRoomFree(allDay, room)) {
            return roomScheduleManager.setUnavailability(room, allDay);
        } else {
            throw new RuntimeException("Couldn't set room unavailability: room is occupied that day");
        }

    }

    @Transactional
    public void deleteDayUnavailability(Long roomId, Date day) {
        this.roomScheduleManager.deleteAllTypeOfEventsBetween(roomId, ERoomStatus.ROOM_UNAVAILABLE, day);
    }
}
