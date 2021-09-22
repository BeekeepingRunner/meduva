package com.szusta.meduva.controller;

import com.szusta.meduva.model.EquipmentModel;
import com.szusta.meduva.payload.request.NewEqModelRequest;
import com.szusta.meduva.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    private EquipmentService equipmentService;

    @Autowired
    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
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
    public EquipmentModel saveNewEqModel(@RequestBody @Valid NewEqModelRequest eqModelRequest) {
        return equipmentService.createModelWithItems(eqModelRequest);
    }

    @DeleteMapping("model/{id}")
    public void deleteModel(@PathVariable Long id) {
        this.equipmentService.markModelAsDeleted(id);
    }
}
