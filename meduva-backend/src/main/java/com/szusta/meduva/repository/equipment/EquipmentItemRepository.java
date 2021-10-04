package com.szusta.meduva.repository.equipment;

import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.repository.undeletable.UndeletableWithNameRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentItemRepository extends UndeletableWithNameRepository<EquipmentItem> {

}
