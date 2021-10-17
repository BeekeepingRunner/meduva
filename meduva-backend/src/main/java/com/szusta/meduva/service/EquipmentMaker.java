package com.szusta.meduva.service;

import com.szusta.meduva.exception.EntityRecordNotFoundException;
import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.model.equipment.EquipmentModel;
import com.szusta.meduva.payload.request.NewEqModelRequest;
import com.szusta.meduva.repository.RoomRepository;
import com.szusta.meduva.repository.ServiceRepository;
import com.szusta.meduva.repository.equipment.EquipmentItemRepository;
import com.szusta.meduva.repository.equipment.EquipmentModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class EquipmentMaker {

    private ServiceRepository serviceRepository;
    private EquipmentModelRepository equipmentModelRepository;
    private RoomRepository roomRepository;
    private EquipmentItemRepository equipmentItemRepository;

    @Autowired
    public EquipmentMaker(ServiceRepository serviceRepository,
                          EquipmentModelRepository equipmentModelRepository,
                          RoomRepository roomRepository,
                          EquipmentItemRepository equipmentItemRepository) {
        this.serviceRepository = serviceRepository;
        this.equipmentModelRepository = equipmentModelRepository;
        this.roomRepository = roomRepository;
        this.equipmentItemRepository = equipmentItemRepository;
    }

    @Transactional
    public EquipmentModel createModelWithItems(NewEqModelRequest eqModelRequest) {

        String modelName = eqModelRequest.getModelName();
        EquipmentModel eqModel = new EquipmentModel(modelName, true, false);

        // connect services to the model
        List<Long> servicesIds = eqModelRequest.getServicesIds();
        List<com.szusta.meduva.model.Service> services = serviceRepository.findAllById(servicesIds);

        // serviceToEqModelService.connectServicesToTheModel(eqModel, services);
        eqModel.setServices(services);
        equipmentModelRepository.save(eqModel);

        // connect rooms with services
        List<Long> roomsIds = eqModelRequest.getSelectedRoomsIds();
        List<Room> rooms = findRoomsByIdsInOrder(roomsIds);
        rooms = connectRoomsWithServices(rooms, services);

        // create items and connect them with rooms
        int itemCount = eqModelRequest.getItemCount();
        List<EquipmentItem> eqItems = createItemsWithNames(itemCount, modelName);
        eqItems = connectItemsWithRooms(eqItems, rooms);

        // connect model with items and save
        eqItems = saveItemsWithModel(eqItems, eqModel);
        eqModel.setItems(eqItems);
        return equipmentModelRepository.save(eqModel);
    }

    private List<Room> findRoomsByIdsInOrder(List<Long> roomsIds) {

        List<Room> rooms = new ArrayList<>();
        for (Long id : roomsIds) {
            Room room = roomRepository.findById(id)
                    .orElseThrow(() -> new EntityRecordNotFoundException("Room not found with id : " + id));
            rooms.add(room);
        }
        return rooms;
    }

    private List<Room> connectRoomsWithServices(List<Room> rooms, List<com.szusta.meduva.model.Service> services) {
        for (Room room : rooms) {
            room.setServices(services);
            room = roomRepository.save(room);
        }
        return rooms;
    }

    private List<EquipmentItem> createItemsWithNames(int itemCount, String modelName) {
        List<EquipmentItem> eqItems = new ArrayList<>(itemCount);
        for (int i = 0; i < itemCount; ++i) {
            // modelName_id, where id >= 1
            String itemName = modelName + "_" + (i + 1);
            eqItems.add(new EquipmentItem(itemName, true, false));
        }
        return eqItems;
    }

    private List<EquipmentItem> saveItemsWithModel(List<EquipmentItem> items, EquipmentModel model) {
        for (EquipmentItem item: items) {
            item.setEquipmentModel(model);
            item = equipmentItemRepository.save(item);
        }
        return items;
    }

    private List<EquipmentItem> connectItemsWithRooms(List<EquipmentItem> items, List<Room> rooms) {
        // we have to connect elements of two collections collaterally
        int i = 0;
        for (EquipmentItem item : items) {
            item.setRoom(rooms.get(i));
            ++i;
        }
        return items;
    }
}
