package com.szusta.meduva.controller;

import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.model.equipment.EquipmentModel;
import com.szusta.meduva.payload.request.NewEqModelRequest;
import com.szusta.meduva.service.equipment.EquipmentMaker;
import com.szusta.meduva.service.equipment.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    private EquipmentService equipmentService;
    private EquipmentMaker equipmentMaker;

    @Autowired
    public EquipmentController(EquipmentService equipmentService,
                               EquipmentMaker equipmentMaker) {
        this.equipmentService = equipmentService;
        this.equipmentMaker = equipmentMaker;
    }

    @GetMapping("/models/all")
    public List<EquipmentModel> findAllEquipmentModels() {
        return this.equipmentService.findAllEquipmentModels();
    }

    @GetMapping("/models/all/undeleted")
    public List<EquipmentModel> findAllUndeletedEquipmentModels() {
        return this.equipmentService.findAllUndeletedEquipmentModels();
    }

    @GetMapping("/model/{id}")
    public EquipmentModel findModelById(@PathVariable Long id) {
        return this.equipmentService.findModelById(id);
    }

    @GetMapping("/item/{id}")
    public EquipmentItem findItemById(@PathVariable Long id) {
        return this.equipmentService.findItemById(id);
    }

    @PostMapping("/model/new")
    public EquipmentModel saveNewEqModel(@RequestBody @Valid NewEqModelRequest eqModelRequest) {
        return equipmentMaker.createModelWithItems(eqModelRequest);
    }

    @GetMapping("/model/doesExistWithName/{modelName}")
    public boolean doesExist(@PathVariable String modelName) {
        return equipmentService.doesModelExistByName(modelName);
    }

    @DeleteMapping("model/{id}")
    public void deleteModel(@PathVariable Long id) {
        this.equipmentService.markModelAsDeleted(id);
    }
}
