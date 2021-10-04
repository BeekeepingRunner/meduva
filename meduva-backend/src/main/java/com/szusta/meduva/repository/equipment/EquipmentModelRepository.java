package com.szusta.meduva.repository.equipment;

import com.szusta.meduva.model.equipment.EquipmentModel;
import com.szusta.meduva.repository.undeletable.UndeletableWithNameRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentModelRepository extends UndeletableWithNameRepository<EquipmentModel> {

}
