package com.szusta.meduva.service;

import com.szusta.meduva.exception.AlreadyExistsException;
import com.szusta.meduva.model.EquipmentItem;
import com.szusta.meduva.model.EquipmentModel;
import com.szusta.meduva.repository.EquipmentItemRepository;
import com.szusta.meduva.repository.EquipmentModelRepository;
import com.szusta.meduva.util.UndeletableWithNameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    public EquipmentModel temporarilySave(EquipmentModel equipmentModel) {
        return this.equipmentModelRepository.save(equipmentModel);
    }

    public EquipmentModel saveModel(EquipmentModel model) {
        if (UndeletableWithNameUtils.canBeSaved(this.equipmentModelRepository, model.getName())) {
            return this.equipmentModelRepository.save(model);
        } else
            throw new AlreadyExistsException("Equipment model already exists with name: " + model.getName());
    }

    public EquipmentItem saveItem(EquipmentItem eqItem) {
        if (UndeletableWithNameUtils.canBeSaved(this.equipmentItemRepository, eqItem.getName())) {
            return this.equipmentItemRepository.save(eqItem);
        } else
            throw new AlreadyExistsException("Equipment model already exists with name: " + eqItem.getName());
    }

    @Transactional
    public void markModelAsDeleted(Long id) {
        // TODO: mark all equipment items tied with this model as deleted
        // UndeletableWithNameUtils.markAsDeleted(this.equipmentModelRepository, id);
    }
}
