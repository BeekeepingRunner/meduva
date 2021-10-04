package com.szusta.meduva.service.entityconnections;

import com.szusta.meduva.exception.EntityRecordNotFoundException;
import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.model.Room;
import com.szusta.meduva.repository.equipment.EquipmentItemRepository;
import com.szusta.meduva.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomToEqItemService {

    private RoomRepository roomRepository;
    private EquipmentItemRepository equipmentItemRepository;

    @Autowired
    public RoomToEqItemService(RoomRepository roomRepository,
                               EquipmentItemRepository equipmentItemRepository) {
        this.roomRepository = roomRepository;
        this.equipmentItemRepository = equipmentItemRepository;
    }

    public List<Room> findRoomsByIdsInOrder(List<Long> roomsIds) {

        List<Room> rooms = new ArrayList<>();
        for (Long id : roomsIds) {
            Room room = roomRepository.findById(id)
                    .orElseThrow(() -> new EntityRecordNotFoundException("Room not found with id : " + id));
            rooms.add(room);
        }
        return rooms;
    }

    public List<EquipmentItem> connectItemsWithRooms(List<EquipmentItem> items, List<Room> rooms) {
        // we have to connect elements of two collections collaterally
        int i = 0;
        for (EquipmentItem item : items) {
            item.setRoom(rooms.get(i));
            ++i;
        }
        return items;
    }

    public void disconnectItems(Room room) {
        List<EquipmentItem> items = room.getEquipmentItems();
        items.forEach(item -> {
            item.deactivate();
            item.setRoom(null);
            equipmentItemRepository.save(item);
        });
    }
}
