package com.szusta.meduva.service;

import com.szusta.meduva.exception.AlreadyExistsException;
import com.szusta.meduva.exception.EntityRecordNotFoundException;
import com.szusta.meduva.model.Room;
import com.szusta.meduva.repository.RoomRepository;
import com.szusta.meduva.service.equipment.ItemsDisconnector;
import com.szusta.meduva.util.UndeletableWithNameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RoomService {

    private RoomRepository roomRepository;
    private ItemsDisconnector itemsDisconnector;

    @Autowired
    public RoomService(RoomRepository roomRepository,
                       ItemsDisconnector itemsDisconnector) {
        this.roomRepository = roomRepository;
        this.itemsDisconnector = itemsDisconnector;
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
}
