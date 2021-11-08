package com.szusta.meduva.service.equipment;

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
import java.util.Optional;

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
        
        // deconstruct request
        String modelName = eqModelRequest.getModelName();
        List<Long> servicesIds = eqModelRequest.getServicesIds();
        List<Long> roomsIds = eqModelRequest.getSelectedRoomsIds();
        int itemCount = eqModelRequest.getItemCount();

        // get necessary entities
        EquipmentModel eqModel = new EquipmentModel(modelName, true, false);
        List<com.szusta.meduva.model.Service> services = serviceRepository.findAllById(servicesIds);
        List<Room> rooms = findRoomsByIdsInOrder(roomsIds);
        List<EquipmentItem> eqItems = createItemsWithNames(itemCount, modelName);

        eqModel = connect(eqModel, services);
        rooms = connect(rooms, services);
        eqItems = connectInParallel(eqItems, rooms);
        eqItems = connect(eqItems, eqModel);

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

    private List<EquipmentItem> createItemsWithNames(int itemCount, String modelName) {
        List<EquipmentItem> eqItems = new ArrayList<>(itemCount);
        for (int i = 0; i < itemCount; ++i) {
            // modelName_id, where id >= 1
            String itemName = modelName + "_" + (i + 1);
            eqItems.add(new EquipmentItem(itemName, true, false));
        }
        return eqItems;
    }

    private EquipmentModel connect(EquipmentModel eqModel, List<com.szusta.meduva.model.Service> services) {
        eqModel.setServices(services);
        return equipmentModelRepository.save(eqModel);
    }
    
    private List<Room> connect(List<Room> rooms, List<com.szusta.meduva.model.Service> services) {
        for (Room room : rooms) {
            room.setServices(services);
            room = roomRepository.save(room);
        }
        return rooms;
    }

    private List<EquipmentItem> connectInParallel(List<EquipmentItem> items, List<Room> rooms) {
        // we have to connect elements of two collections collaterally
        int i = 0;
        for (EquipmentItem item : items) {
            item.setRoom(rooms.get(i));
            ++i;
        }
        return items;
    }

    private List<EquipmentItem> connect(List<EquipmentItem> items, EquipmentModel model) {
        for (EquipmentItem item: items) {
            item.setEquipmentModel(model);
            item = equipmentItemRepository.save(item);
        }
        return items;
    }

    public EquipmentModel connectModelWithServices(NewEqModelRequest eqModelRequest) {

        String modelName = eqModelRequest.getModelName();
        List<Long> servicesIds = eqModelRequest.getServicesIds();

        Optional<EquipmentModel> eqModelOptional = equipmentModelRepository.findByName(modelName);
        EquipmentModel eqModel = eqModelOptional.get();
        List<com.szusta.meduva.model.Service> services = serviceRepository.findAllById(servicesIds);

        eqModel = connect(eqModel, services);

        return equipmentModelRepository.save(eqModel);
    }
}
