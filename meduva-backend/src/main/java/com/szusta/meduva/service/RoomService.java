package com.szusta.meduva.service;

import com.szusta.meduva.exception.AlreadyExistsException;
import com.szusta.meduva.model.Room;
import com.szusta.meduva.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class RoomService {

    private RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> findAllRooms() {
        return this.roomRepository.findAll();
    }

    public List<Room> findAllUnDeletedRooms() {
        List<Room> rooms = this.roomRepository.findAll();
        return rooms.stream()
                .filter(room -> !room.isDeleted())
                .collect(Collectors.toList());
    }

    public Room save(Room room) {

        String roomName = room.getName();
        if (this.roomRepository.existsByName(roomName) && isNotDeleted(roomName)) {
            throw new AlreadyExistsException("Room already exists with name: " + room.getName());
        } else
            return this.roomRepository.save(room);
    }

    private boolean isNotDeleted(String roomName) {
        Room room = this.roomRepository.findByName(roomName)
                .orElseThrow(() -> new AlreadyExistsException("Room not found with name: " + roomName));

        return !room.isDeleted();
    }

    public void deleteById(Long id) {
        this.roomRepository.deleteById(id);
    }

    @Transactional
    public void markAsDeleted(Long id) {
        Room room = this.roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id : " + id));

        room.setDeleted(true);
        this.roomRepository.save(room);
    }
}
