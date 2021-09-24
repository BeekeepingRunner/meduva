package com.szusta.meduva.service;

import com.szusta.meduva.exception.EntityRecordNotFoundException;
import com.szusta.meduva.model.EquipmentItem;
import com.szusta.meduva.model.EquipmentModel;
import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.Service;
import com.szusta.meduva.payload.request.NewEqModelRequest;
import com.szusta.meduva.repository.EquipmentItemRepository;
import com.szusta.meduva.repository.EquipmentModelRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class EquipmentService {

    private EquipmentModelRepository equipmentModelRepository;
    private EquipmentItemRepository equipmentItemRepository;
    private ServicesService servicesService;
    private RoomService roomService;

    @Autowired
    public EquipmentService(
            EquipmentModelRepository equipmentModelRepository,
            EquipmentItemRepository equipmentItemRepository,
            ServicesService servicesService,
            RoomService roomService
    ) {
        this.equipmentModelRepository = equipmentModelRepository;
        this.equipmentItemRepository = equipmentItemRepository;
        this.servicesService = servicesService;
        this.roomService = roomService;
    }

    public List<EquipmentModel> findAllEquipmentModels() {
        return this.equipmentModelRepository.findAll();
    }

    public List<EquipmentModel> findAllUndeletedEquipmentModels() {
        return this.equipmentModelRepository.findAllUndeleted();
    }

    public EquipmentModel findModelById(Long id) {
        return this.equipmentModelRepository.findById(id)
                .orElseThrow(() -> new EntityRecordNotFoundException("Equipment model not found with id : " + id));
    }

    public boolean doesModelExistByName(String modelName) {
        return this.equipmentModelRepository.existsByName(modelName);
    }

    @Transactional
    public EquipmentModel createModelWithItems(NewEqModelRequest eqModelRequest) {

        // connect services to new model
        List<Long> servicesIds = eqModelRequest.getServicesIds();
        List<Service> services = servicesService.findWithIds(servicesIds);
        String modelName = eqModelRequest.getModelName();
        EquipmentModel eqModel = createModelWithServices(modelName, services);

        // connect rooms with services and save
        List<Long> roomsIds = eqModelRequest.getSelectedRoomsIds();
        List<Room> rooms = roomService.findWithIdsInOrder(roomsIds);
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

    private EquipmentModel createModelWithServices(String modelName, List<Service> services) {
        EquipmentModel eqModel = new EquipmentModel(modelName, true, false);
        eqModel.setServices(services);
        return equipmentModelRepository.save(eqModel);
    }

    private List<Room> connectRoomsWithServices(List<Room> rooms, List<Service> services) {
        for (Room room : rooms) {
            room.setServices(services);
            room = roomService.update(room);
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

    private List<EquipmentItem> connectItemsWithRooms(List<EquipmentItem> items, List<Room> rooms) {
        int i = 0;
        for (EquipmentItem item : items) {
            item.setRoom(rooms.get(i));
            ++i;
        }
        return items;
    }

    private List<EquipmentItem> saveItemsWithModel(List<EquipmentItem> items, EquipmentModel model) {
        for (EquipmentItem item: items) {
            item.setEquipmentModel(model);
            item = equipmentItemRepository.save(item);
        }
        return items;
    }

    @Transactional
    public void markModelAsDeleted(Long id) {
        // TODO: mark all equipment items tied with this model as deleted
        // UndeletableWithNameUtils.markAsDeleted(this.equipmentModelRepository, id);
    }
}
