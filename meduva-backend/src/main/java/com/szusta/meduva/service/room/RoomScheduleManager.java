package com.szusta.meduva.service.room;

import com.szusta.meduva.exception.EntityRecordNotFoundException;
import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.schedule.RoomSchedule;
import com.szusta.meduva.model.schedule.status.RoomStatus;
import com.szusta.meduva.model.schedule.status.enums.ERoomStatus;
import com.szusta.meduva.payload.TimeRange;
import com.szusta.meduva.repository.schedule.room.RoomScheduleRepository;
import com.szusta.meduva.repository.schedule.room.RoomStatusRepository;
import com.szusta.meduva.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RoomScheduleManager {

    private RoomStatusRepository roomStatusRepository;
    private RoomScheduleRepository roomScheduleRepository;

    @Autowired
    public RoomScheduleManager(RoomStatusRepository roomStatusRepository,
                               RoomScheduleRepository roomScheduleRepository) {
        this.roomStatusRepository = roomStatusRepository;
        this.roomScheduleRepository = roomScheduleRepository;
    }

    public RoomSchedule setUnavailability(Room room, TimeRange allDay) {
        Long roomStatusId = ERoomStatus.ROOM_UNAVAILABLE.getValue();
        RoomStatus roomStatus = roomStatusRepository.findById(roomStatusId)
                .orElseThrow(() -> new EntityRecordNotFoundException("Cannot set room unavailability: room status not found in DB with id : " + roomStatusId));

        RoomSchedule roomSchedule = new RoomSchedule(room, allDay, roomStatus);
        return roomScheduleRepository.save(roomSchedule);
    }

    public void deleteAllTypeOfEventsBetween(Long roomId, ERoomStatus status, Date day) {
        Date dayStart = TimeUtils.getDayStart(day).getTime();
        Date dayEnd = TimeUtils.getDayEnd(day).getTime();
        roomScheduleRepository.deleteByRoomIdBetween(roomId, status.getValue(), dayStart, dayEnd);
    }
}
