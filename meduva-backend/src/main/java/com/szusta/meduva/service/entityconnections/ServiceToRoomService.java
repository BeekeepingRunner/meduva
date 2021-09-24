package com.szusta.meduva.service.entityconnections;

import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.Service;
import com.szusta.meduva.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceToRoomService {

    private RoomRepository roomRepository;

    @Autowired
    public ServiceToRoomService(
            RoomRepository roomRepository
    ) {
        this.roomRepository = roomRepository;
    }

    public List<Room> connectRoomsWithServices(List<Room> rooms, List<Service> services) {
        for (Room room : rooms) {
            room.setServices(services);
            room = roomRepository.save(room);
        }
        return rooms;
    }
}
