package com.szusta.meduva.controller;

import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.model.equipment.EquipmentModel;
import com.szusta.meduva.model.schedule.EquipmentSchedule;
import com.szusta.meduva.payload.TimeRange;
import com.szusta.meduva.payload.request.DeleteDailyHoursRequest;
import com.szusta.meduva.payload.request.add.NewEqModelRequest;
import com.szusta.meduva.service.equipment.EquipmentMaker;
import com.szusta.meduva.service.equipment.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
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

    @PostMapping("/model/connect")
    public EquipmentModel configureEqModelConnections(@RequestBody @Valid NewEqModelRequest eqModelRequest) {
        return equipmentMaker.connectModelWithServices(eqModelRequest);
    }

    @GetMapping("/model/doesExistWithName/{modelName}")
    public boolean doesExist(@PathVariable String modelName) {
        return equipmentService.doesModelExistByName(modelName);
    }

    @DeleteMapping("model/{id}")
    public void deleteModel(@PathVariable Long id) {
        this.equipmentService.markModelAsDeleted(id);
    }


    @DeleteMapping("/models/all")
    public void deleteAllModelsPermanently() {
            this.equipmentService.deleteAllModelsPermanently();
        }

    @PostMapping("item/get-weekly-unavailability/{itemId}")
    public List<TimeRange> getItemWeeklyUnavailability(@PathVariable Long itemId, @RequestBody TimeRange weekBoundaries) {
        EquipmentItem item = equipmentService.findItemById(itemId);
        return equipmentService.getItemWeeklyUnavailability(item, weekBoundaries);
    }

    @PostMapping("item/set-day-unavailability/{itemId}")
    public TimeRange setItemDayUnavailability(@PathVariable Long itemId, @RequestBody Date day) {
        EquipmentSchedule eqSchedule = equipmentService.setItemDayUnavailability(itemId, day);
        return new TimeRange(eqSchedule.getTimeFrom(), eqSchedule.getTimeTo());
    }

    @DeleteMapping("item/delete-day-unavailability/{itemId}")
    public void deleteDayUnavailability(@PathVariable Long itemId, @RequestBody DeleteDailyHoursRequest request){
        Date day = request.getDay();
        this.equipmentService.deleteDayUnavailability(itemId, day);
    }
}
