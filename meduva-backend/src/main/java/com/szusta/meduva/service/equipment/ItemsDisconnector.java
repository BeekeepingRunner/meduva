package com.szusta.meduva.service.equipment;

import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.repository.equipment.EquipmentItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemsDisconnector {

    private EquipmentItemRepository equipmentItemRepository;

    @Autowired
    public ItemsDisconnector(EquipmentItemRepository equipmentItemRepository) {
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
