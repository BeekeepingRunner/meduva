package com.szusta.meduva.repository;

import com.szusta.meduva.model.EquipmentModel;
import com.szusta.meduva.repository.undeletable.UndeletableWithNameRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentModelRepository extends UndeletableWithNameRepository<EquipmentModel> {

}
