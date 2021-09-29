package com.szusta.meduva.repository;

import com.szusta.meduva.model.EquipmentItem;
import com.szusta.meduva.repository.undeletable.UndeletableWithNameRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentItemRepository extends UndeletableWithNameRepository<EquipmentItem> {

}
