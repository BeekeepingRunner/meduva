package com.szusta.meduva.service;

import com.szusta.meduva.exception.AlreadyExistsException;
import com.szusta.meduva.exception.EntityRecordNotFoundException;
import com.szusta.meduva.model.Room;
import com.szusta.meduva.repository.RoomRepository;
import com.szusta.meduva.util.UndeletableWithNameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    public Room saveNewRoom(Room room) {

        if (UndeletableWithNameUtils.canBeSaved(this.roomRepository, room.getName())) {
            return this.roomRepository.save(room);
        } else
            throw new AlreadyExistsException("Room already exists with name: " + room.getName());
    }

    public void deleteById(Long id) {
        this.roomRepository.deleteById(id);
    }

    @Transactional
    public void markAsDeleted(Long id) {
        UndeletableWithNameUtils.markAsDeletedOnly(this.roomRepository, id);
    }

}
