package com.szusta.meduva.service.entityconnections;

import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.repository.equipment.EquipmentItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomToEqItemService {

    private EquipmentItemRepository equipmentItemRepository;

    @Autowired
    public RoomToEqItemService(EquipmentItemRepository equipmentItemRepository) {
        this.equipmentItemRepository = equipmentItemRepository;
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
