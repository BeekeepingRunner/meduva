package com.szusta.meduva.service.entityconnections;

import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.model.equipment.EquipmentModel;
import com.szusta.meduva.repository.equipment.EquipmentItemRepository;
import com.szusta.meduva.repository.equipment.EquipmentModelRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ServiceToEqModelService {

    private EquipmentModelRepository equipmentModelRepository;
    private EquipmentItemRepository equipmentItemRepository;

    @Autowired
    public ServiceToEqModelService(
            EquipmentModelRepository equipmentModelRepository,
            EquipmentItemRepository equipmentItemRepository
    ) {
        this.equipmentModelRepository = equipmentModelRepository;
        this.equipmentItemRepository = equipmentItemRepository;
    }

    public void deactivateModelsWithLastService(Service service) {

        List<EquipmentModel> models = service.getEquipmentModel();
        List<EquipmentModel> modelsToDeactivate = getModelsToDeactivate(models);
        List<EquipmentModel> deactivatedModels = deactivateModels(modelsToDeactivate);
        deactivateItems(deactivatedModels);
    }

    private List<EquipmentModel> getModelsToDeactivate(List<EquipmentModel> models) {
        List<EquipmentModel> modelsToDeactivate = new ArrayList<>();
        models.forEach(model -> {
            if (model.getServices().size() == 1) {
                modelsToDeactivate.add(model);
            }
        });
        return modelsToDeactivate;
    }

    private List<EquipmentModel> deactivateModels(List<EquipmentModel> modelsToDeactivate) {
        return modelsToDeactivate.stream().map(model -> {
            model.deactivate();
            return equipmentModelRepository.save(model);
        }).collect(Collectors.toList());
    }

    private void deactivateItems(List<EquipmentModel> deactivatedModels) {
        deactivatedModels.forEach(deactivatedModel -> {
            List<EquipmentItem> equipmentItems = deactivatedModel.getItems();
            equipmentItems.forEach(item -> {
                item.deactivate();
                this.equipmentItemRepository.save(item);
            });
        });
    }
}
