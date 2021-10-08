package com.szusta.meduva.service;

import com.szusta.meduva.exception.EntityRecordNotFoundException;
import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.model.equipment.EquipmentModel;
import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.Service;
import com.szusta.meduva.payload.request.NewEqModelRequest;
import com.szusta.meduva.repository.equipment.EquipmentItemRepository;
import com.szusta.meduva.repository.equipment.EquipmentModelRepository;
import com.szusta.meduva.service.entityconnections.RoomToEqItemService;
import com.szusta.meduva.service.entityconnections.ServiceToEqModelService;
import com.szusta.meduva.service.entityconnections.ServiceToRoomService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@org.springframework.stereotype.Service
public class EquipmentService {

    private EquipmentModelRepository equipmentModelRepository;
    private EquipmentItemRepository equipmentItemRepository;

    private ServiceToEqModelService serviceToEqModelService;
    private RoomToEqItemService roomToEqItemService;
    private ServiceToRoomService serviceToRoomService;

    @Autowired
    public EquipmentService(
            EquipmentModelRepository equipmentModelRepository,
            EquipmentItemRepository equipmentItemRepository,
            ServiceToEqModelService serviceToEqModelService,
            RoomToEqItemService roomToEqItemService,
            ServiceToRoomService serviceToRoomService
    ) {
        this.equipmentModelRepository = equipmentModelRepository;
        this.equipmentItemRepository = equipmentItemRepository;
        this.serviceToEqModelService = serviceToEqModelService;
        this.roomToEqItemService = roomToEqItemService;
        this.serviceToRoomService = serviceToRoomService;
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

    public EquipmentItem findItemById(Long id) {
        return this.equipmentItemRepository.findById(id)
                .orElseThrow(() -> new EntityRecordNotFoundException("Equipment item not found with id : " + id));
    }

    public boolean doesModelExistByName(String modelName) {
        return this.equipmentModelRepository.existsByName(modelName);
    }

    @Transactional
    public EquipmentModel createModelWithItems(NewEqModelRequest eqModelRequest) {

        String modelName = eqModelRequest.getModelName();
        EquipmentModel eqModel = new EquipmentModel(modelName, true, false);

        // connect services to the model
        List<Long> servicesIds = eqModelRequest.getServicesIds();
        List<Service> services = serviceToEqModelService.findAllById(servicesIds);
        serviceToEqModelService.connectServicesToTheModel(eqModel, services);

        // connect rooms with services
        List<Long> roomsIds = eqModelRequest.getSelectedRoomsIds();
        List<Room> rooms = roomToEqItemService.findRoomsByIdsInOrder(roomsIds);
        rooms = serviceToRoomService.connectRoomsWithServices(rooms, services);

        // create items and connect them with rooms
        int itemCount = eqModelRequest.getItemCount();
        List<EquipmentItem> eqItems = createItemsWithNames(itemCount, modelName);
        eqItems = roomToEqItemService.connectItemsWithRooms(eqItems, rooms);

        // connect model with items and save
        eqItems = saveItemsWithModel(eqItems, eqModel);
        eqModel.setItems(eqItems);
        return equipmentModelRepository.save(eqModel);
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

    @Transactional
    public void markModelAsDeleted(Long id) {
        EquipmentModel model = equipmentModelRepository.findById(id)
                .orElseThrow(() -> new EntityRecordNotFoundException("Equipment model not found with id : " + id));

        markModelItemsAsDeleted(model);
        model.setServices(Collections.emptyList());
        model.deactivate();
        model.markAsDeleted();
        equipmentModelRepository.save(model);
    }

    private void markModelItemsAsDeleted(EquipmentModel model) {
        List<EquipmentItem> itemsToDelete = model.getItems();
        itemsToDelete.forEach(item -> {
            // item.setRoom(null) <- will be that necessary?
            item.deactivate();
            item.markAsDeleted();
            equipmentItemRepository.save(item);
        });
    }
}
