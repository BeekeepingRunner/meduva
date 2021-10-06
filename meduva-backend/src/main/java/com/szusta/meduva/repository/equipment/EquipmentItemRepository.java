package com.szusta.meduva.repository.equipment;

import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.repository.undeletable.UndeletableWithNameRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentItemRepository extends UndeletableWithNameRepository<EquipmentItem> {

    @Query(
            value = "SELECT * FROM equipment_item "
            + "INNER JOIN equipment_model model ON equipment_item.equipment_model_id = model.id "
            + "INNER JOIN service_equipment_model sem ON sem.equipment_model_id = model.id "
            + "WHERE sem.service_id = ?1",
            nativeQuery = true
    )
    List<EquipmentItem> findAllSuitableForService(Long serviceId);
}
