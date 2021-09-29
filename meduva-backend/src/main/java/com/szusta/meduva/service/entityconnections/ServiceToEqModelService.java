package com.szusta.meduva.service.entityconnections;

import com.szusta.meduva.model.EquipmentItem;
import com.szusta.meduva.model.EquipmentModel;
import com.szusta.meduva.model.Service;
import com.szusta.meduva.repository.EquipmentItemRepository;
import com.szusta.meduva.repository.EquipmentModelRepository;
import com.szusta.meduva.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Service> findAllById(List<Long> servicesIds) {
        return serviceRepository.findAllById(servicesIds);
    }

    public EquipmentModel connectServicesToTheModel(EquipmentModel model, List<Service> services) {
        model.setServices(services);
        return this.equipmentModelRepository.save(model);
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
