package com.szusta.meduva.controller;

import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.model.equipment.EquipmentModel;
import com.szusta.meduva.payload.request.NewEqModelRequest;
import com.szusta.meduva.service.RoomService;
import com.szusta.meduva.service.ServicesService;
import com.szusta.meduva.service.equipment.EquipmentMaker;
import com.szusta.meduva.service.equipment.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/creator")
public class CreatorController {

    private EquipmentService equipmentService;
    private RoomService roomService;
    private ServicesService servicesService;

    @Autowired
    public CreatorController(EquipmentService equipmentService,
                             RoomService roomService,
                             ServicesService servicesService) {
        this.equipmentService = equipmentService;
        this.roomService = roomService;
        this.servicesService = servicesService;
    }

    @DeleteMapping("/all")
    public void deleteAllModelsPermanently() {
        this.equipmentService.deleteAllModelsPermanently();
        this.roomService.deleteAllRoomsPermanently();
        this.servicesService.deleteAllServicesPermanently();
    }
}
