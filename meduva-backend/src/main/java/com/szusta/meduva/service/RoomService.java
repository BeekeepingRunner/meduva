package com.szusta.meduva.service;

import com.szusta.meduva.exception.AlreadyExistsException;
import com.szusta.meduva.exception.EntityRecordNotFoundException;
import com.szusta.meduva.model.Room;
import com.szusta.meduva.repository.RoomRepository;
import com.szusta.meduva.util.UndeletableWithNameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

    private RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
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

    public List<Room> findWithIdsInOrder(List<Long> roomsIds) {

        List<Room> rooms = new ArrayList<>();
        for (Long id : roomsIds) {
            Room room = roomRepository.findById(id)
                            .orElseThrow(() -> new EntityRecordNotFoundException("Room not found with id : " + id));
            rooms.add(room);
        }
        return rooms;
    }

    public Room saveNewRoom(Room room) {

        if (UndeletableWithNameUtils.canBeSaved(this.roomRepository, room.getName())) {
            return this.roomRepository.save(room);
        } else
            throw new AlreadyExistsException("Room already exists with name: " + room.getName());
    }

    public Room update(Room room) {
        return this.roomRepository.save(room);
    }

    public void deleteById(Long id) {
        this.roomRepository.deleteById(id);
    }

    @Transactional
    public void markAsDeleted(Long id) {
        UndeletableWithNameUtils.markAsDeleted(this.roomRepository, id);
    }

}
