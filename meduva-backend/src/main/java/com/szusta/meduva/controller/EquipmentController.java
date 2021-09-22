package com.szusta.meduva.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.szusta.meduva.model.EquipmentItem;
import com.szusta.meduva.model.EquipmentModel;
import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.Service;
import com.szusta.meduva.payload.request.NewEqModelRequest;
import com.szusta.meduva.service.EquipmentService;
import com.szusta.meduva.service.RoomService;
import com.szusta.meduva.service.ServicesService;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    private EquipmentService equipmentService;

    // temporary
    private RoomService roomService;
    private ServicesService servicesService;

    @Autowired
    public EquipmentController(EquipmentService equipmentService, RoomService roomService, ServicesService servicesService) {
        this.equipmentService = equipmentService;
        this.roomService = roomService;
        this.servicesService = servicesService;
    }

    @GetMapping("/models/all")
    public List<EquipmentModel> findAllEquipmentModels() {
        return this.equipmentService.findAllEquipmentModels();
    }

    @GetMapping("models/all/undeleted")
    public List<EquipmentModel> findAllUndeletedEquipmentModels() {
        return this.equipmentService.findAllUndeletedEquipmentModels();
    }

    @PostMapping("/model/new")
    // TODO: refactor and delete this adnotation
    @Transactional
    public EquipmentModel saveNewEqModel(@RequestBody @Valid NewEqModelRequest eqModelRequest) {

        // create new model
        String modelName = eqModelRequest.getModelName();
        EquipmentModel eqModel = new EquipmentModel(modelName, false);

        // connect model with services
        Set<Service> services = new HashSet<>();
        List<Long> servicesIds = eqModelRequest.getServicesIds();
        servicesIds.forEach(serviceId -> {
            Service service = servicesService.findById(serviceId);
            services.add(service);
        });
        eqModel.setServices(services);

        // save model
        eqModel = equipmentService.temporarilySave(eqModel);

        // create items with names
        int itemCount = eqModelRequest.getItemCount();
        List<EquipmentItem> eqItems = new ArrayList<>(itemCount);
        for (int i = 0; i < itemCount; ++i) {
            // modelName_id, where id >= 1
            String itemName = modelName + "_" + (i + 1);
            eqItems.add(new EquipmentItem(itemName, false));
        }

        // connect items with rooms
        List<Long> roomsIds = eqModelRequest.getSelectedRoomsIds();
        for (int i = 0; i < itemCount; ++i) {
            Room room = roomService.findById(roomsIds.get(i));
            eqItems.get(i).setRoom(room);
        }

        // connect items with new model
        for (EquipmentItem item: eqItems) {
            item.setEquipmentModel(eqModel);
        }

        // save items
        for (EquipmentItem item: eqItems) {
            item = equipmentService.saveItem(item);
        }

        // set model items
        eqModel.setItems(eqItems);
        return equipmentService.temporarilySave(eqModel);
    }

    @DeleteMapping("model/{id}")
    public void deleteModel(@PathVariable Long id) {
        this.equipmentService.markModelAsDeleted(id);
    }
}
