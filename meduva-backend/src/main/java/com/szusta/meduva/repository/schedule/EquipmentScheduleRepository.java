package com.szusta.meduva.repository.schedule;

import com.szusta.meduva.model.schedule.EquipmentSchedule;
import com.szusta.meduva.repository.undeletable.UndeletableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EquipmentScheduleRepository extends UndeletableRepository<EquipmentSchedule> {

    @Query(
            value =
                    "select * from equipment_schedule s "
                            +   "where s.equipment_item_id = ?3 and ("
                            +       "("
                            +           "(timestampdiff(MINUTE, time_from, ?2) > 0) and "
                            +           "(timestampdiff(MINUTE, time_from, ?1) <= 0)"
                            +       ") or "
                            +       "("
                            +           "(timestampdiff(MINUTE, time_to, ?1) < 0) and "
                            +           "(timestampdiff(MINUTE, time_to, ?2) >= 0)"
                            +       ")"
                            +   ")",
            nativeQuery = true
    )
    List<? super EquipmentSchedule> findAnyBetween(Date start, Date end, Long eqItemId);
}
