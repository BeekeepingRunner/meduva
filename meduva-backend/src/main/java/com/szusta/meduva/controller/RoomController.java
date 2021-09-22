package com.szusta.meduva.controller;

import com.szusta.meduva.model.Room;
import com.szusta.meduva.payload.request.NewRoomRequest;
import com.szusta.meduva.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room")
public class RoomController {

    private RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/all")
    public List<Room> findAllRooms() {
        return this.roomService.findAllRooms();
    }

    @GetMapping("/all/undeleted")
    public List<Room> findAllUnDeletedRooms() {
        return this.roomService.findAllUnDeletedRooms();
    }

    @PostMapping
    public Room addRoom(@RequestBody NewRoomRequest request) {
        Room room = new Room(
                request.getName(),
                request.getDescription(),
                request.isDeleted()
        );
        return this.roomService.save(room);
    }

    @DeleteMapping("/{id}")
    public void deleteRoom(@PathVariable Long id) {
        this.roomService.markAsDeleted(id);
    }
}
