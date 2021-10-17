package com.szusta.meduva.service.equipment;

import com.szusta.meduva.exception.EntityRecordNotFoundException;
import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.model.equipment.EquipmentModel;
import com.szusta.meduva.repository.equipment.EquipmentItemRepository;
import com.szusta.meduva.repository.equipment.EquipmentModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
public class EquipmentService {

    private EquipmentModelRepository equipmentModelRepository;
    private EquipmentItemRepository equipmentItemRepository;

    @Autowired
    public EquipmentService(
            EquipmentModelRepository equipmentModelRepository,
            EquipmentItemRepository equipmentItemRepository
    ) {
        this.equipmentModelRepository = equipmentModelRepository;
        this.equipmentItemRepository = equipmentItemRepository;
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
