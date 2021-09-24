package com.szusta.meduva.service.entityconnections;

import com.szusta.meduva.model.EquipmentItem;
import com.szusta.meduva.model.EquipmentModel;
import com.szusta.meduva.model.Service;
import com.szusta.meduva.repository.EquipmentItemRepository;
import com.szusta.meduva.repository.EquipmentModelRepository;
import com.szusta.meduva.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class ServiceToEqModelService {

    private EquipmentModelRepository equipmentModelRepository;
    private EquipmentItemRepository equipmentItemRepository;
    private ServiceRepository serviceRepository;

    @Autowired
    public ServiceToEqModelService(
            EquipmentModelRepository equipmentModelRepository,
            EquipmentItemRepository equipmentItemRepository,
            ServiceRepository serviceRepository
    ) {
        this.equipmentModelRepository = equipmentModelRepository;
        this.equipmentItemRepository = equipmentItemRepository;
        this.serviceRepository = serviceRepository;
    }

    public void deactivateModelsWithLastService(Service service) {

        List<EquipmentModel> models = service.getEquipmentModel();

        // deactivate models only
        List<EquipmentModel> modelsToDeactivate = new ArrayList<>();
        models.forEach(model -> {
            if (model.getServices().size() == 1) {
                modelsToDeactivate.add(model);
            }
        });
        modelsToDeactivate.forEach(model -> {
            model.deactivate();
            equipmentModelRepository.save(model);
        });

        // prepare map of models and their items
        // Map<EquipmentModel, EquipmentItem> modelItemMap = new HashMap<>();

        // deactivate items of deactivated models
        modelsToDeactivate.forEach(deactivatedModel -> {

            List<EquipmentItem> equipmentItems = deactivatedModel.getItems();
            equipmentItems.forEach(item -> {
                item.deactivate();
                this.equipmentItemRepository.save(item);
            });
        });
    }

    @Transactional
    public void markModelAsDeleted(Long id) {
        // TODO: mark all equipment items tied with this model as deleted
        // UndeletableWithNameUtils.markAsDeleted(this.equipmentModelRepository, id);
    }
}
