package com.szusta.meduva.controller;

import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.Service;
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
    public List<Room> findAllUndeletedRooms() {
        return this.roomService.findAllUndeletedRooms();
    }

    @PostMapping
    public Room addRoom(@RequestBody NewRoomRequest request) {
        Room room = new Room(
                request.getName(),
                request.getDescription(),
                request.isDeleted()
        );
        return this.roomService.saveNewRoom(room);
    }

    @PostMapping("/{id}/edit-services")
    public Room editServices(@PathVariable final Long id, @RequestBody final List<Service> editedServicesList){
        Room room = this.roomService.findById(id);
        room.setServices(editedServicesList);
        return this.roomService.editRoomServices(room);
    }

    @DeleteMapping("/{id}")
    public void deleteRoom(@PathVariable Long id) {
        this.roomService.markAsDeleted(id);
    }
}
