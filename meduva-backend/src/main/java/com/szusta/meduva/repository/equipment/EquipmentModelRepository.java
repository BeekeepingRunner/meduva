package com.szusta.meduva.repository.equipment;

import com.szusta.meduva.model.equipment.EquipmentModel;
import com.szusta.meduva.repository.undeletable.UndeletableWithNameRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentModelRepository extends UndeletableWithNameRepository<EquipmentModel> {

    @Query(
            value = "SELECT * FROM equipment_model model "
            + "INNER JOIN service_equipment_model sem ON sem.equipment_model_id = model.id "
            + "WHERE sem.service_id = ?1",
            nativeQuery = true
    )
    List<EquipmentModel> findByService(Long serviceId);
}
