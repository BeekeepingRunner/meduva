package com.szusta.meduva.repository.schedule.equipment;

import com.szusta.meduva.model.schedule.status.EquipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentStatusRepository extends JpaRepository<EquipmentStatus, Long> {

}
