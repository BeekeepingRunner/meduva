package com.szusta.meduva.repository.schedule.equipment;

import com.szusta.meduva.model.schedule.EquipmentSchedule;
import com.szusta.meduva.repository.undeletable.UndeletableRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface EquipmentScheduleRepository extends UndeletableRepository<EquipmentSchedule> {

    @Query(
            value =
                    "select * from equipment_schedule s "
                            +   "where s.equipment_item_id = ?1 and "
                            +   "s.equipment_status_id = ?2 and s.time_from >= CURRENT_DATE()",
            nativeQuery = true
    )
    List<EquipmentSchedule> findAllInTheFuture(long itemId, Long eqStatusId);

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
    List<EquipmentSchedule> findAllBetween(Date startTime, Date endTime, long itemId);

    @Query(
            value =
                    "select * from equipment_schedule s "
                            +   "where s.equipment_item_id = ?3 and ("
                            +       "("
                            +           "(timestampdiff(MINUTE, time_from, ?1) > 0) and "
                            +           "(timestampdiff(MINUTE, time_to, ?1) < 0)"
                            +       ") or "
                            +       "("
                            +           "(timestampdiff(MINUTE, time_from, ?1) <= 0) and "
                            +           "(timestampdiff(MINUTE, time_to, ?2) >= 0)"
                            +       ") or "
                            +       "("
                            +           "(timestampdiff(MINUTE, time_from, ?2) > 0) and "
                            +           "(timestampdiff(MINUTE, time_to, ?2) <= 0)"
                            +       ") or "
                            +       "("
                            +           "(timestampdiff(MINUTE, time_from, ?1) > 0) and "
                            +           "(timestampdiff(MINUTE, time_to, ?2) < 0)"
                            +       ")"
                            +   ")",
            nativeQuery = true
    )
    List<EquipmentSchedule> findAllDuring(Date startTime, Date endTime, long itemId);

    @Query(
            value =
                    "select * from equipment_schedule s "
                            +   "where s.equipment_item_id = ?3 and "
                            +   "s.equipment_status_id = ?4 and ("
                            +       "("
                            +           "(timestampdiff(MINUTE, time_from, ?1) > 0) and "
                            +           "(timestampdiff(MINUTE, time_to, ?1) < 0)"
                            +       ") or "
                            +       "("
                            +           "(timestampdiff(MINUTE, time_from, ?1) <= 0) and "
                            +           "(timestampdiff(MINUTE, time_to, ?2) >= 0)"
                            +       ") or "
                            +       "("
                            +           "(timestampdiff(MINUTE, time_from, ?2) > 0) and "
                            +           "(timestampdiff(MINUTE, time_to, ?2) <= 0)"
                            +       ") or "
                            +       "("
                            +           "(timestampdiff(MINUTE, time_from, ?1) > 0) and "
                            +           "(timestampdiff(MINUTE, time_to, ?2) < 0)"
                            +       ")"
                            +   ")",
            nativeQuery = true
    )
    List<EquipmentSchedule> findAllDuring(Date startTime, Date endTime, long itemId, Long eqStatusId);

    @Query(
            value =
                    "select * from equipment_schedule s "
                            +   "where s.equipment_item_id = ?3 and "
                            +   "s.equipment_status_id = ?4 and ("
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
    List<EquipmentSchedule> findAllBetween(Date startTime, Date endTime, long itemId, Long eqStatusId);

    @Modifying
    @Query(
            nativeQuery = true,
            value = "DELETE FROM equipment_schedule es WHERE es.equipment_item_id = ?1 AND "
                    + " timestampdiff(MINUTE, time_from, ?2) <= 0 AND timestampdiff(MINUTE, time_to, ?3) >= 0"
    )
    void deleteByEqItemIdBetween(Long eqItemId, Date start, Date end);

    @Modifying
    @Query(
            nativeQuery = true,
            value = "DELETE FROM equipment_schedule es WHERE es.equipment_item_id = ?1 AND "
                    + " es.time_from = ?2"
    )

    @Transactional
    void deleteByEqItemIdWithStartTime(Long eqItemId, Date startTime);

    @Modifying
    @Query(
            nativeQuery = true,
            value = "DELETE FROM equipment_schedule es WHERE es.equipment_item_id = ?1 AND "
                    + " es.equipment_status_id = ?2 AND "
                    + " timestampdiff(MINUTE, time_from, ?3) <= 0 AND timestampdiff(MINUTE, time_to, ?4) >= 0"
    )
    void deleteByEqItemIdBetween(Long eqItemId, Long eqStatusId, Date start, Date end);
}
