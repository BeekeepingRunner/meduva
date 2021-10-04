package com.szusta.meduva.repository.schedule;

import com.szusta.meduva.model.schedule.EquipmentSchedule;
import com.szusta.meduva.repository.undeletable.UndeletableRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentScheduleRepository extends UndeletableRepository<EquipmentSchedule> {

}
