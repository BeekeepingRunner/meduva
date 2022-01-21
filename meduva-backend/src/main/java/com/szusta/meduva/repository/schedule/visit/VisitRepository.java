package com.szusta.meduva.repository.schedule.visit;

import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.UnregisteredClient;
import com.szusta.meduva.model.schedule.visit.Visit;
import com.szusta.meduva.repository.UnregisteredClientRepository;
import com.szusta.meduva.repository.undeletable.UndeletableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VisitRepository extends UndeletableRepository<Visit> {

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM visit v "
                    + "INNER JOIN user_visit uv ON visit_id = v.id "
                    + "WHERE as_client = 1 AND uv.user_id = ?1 AND v.deleted = 0 "
                    + "ORDER BY v.time_from DESC "
    )
    List<Visit> findAllWhereUserIsClient(Long userClientId);

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM visit v "
                    + "INNER JOIN user_visit uv ON visit_id = v.id "
                    + "WHERE as_client = 0 AND uv.user_id = ?1 AND v.deleted = 0 "
                    + "ORDER BY v.time_from DESC "
    )
    List<Visit> findAllWhereUserIsWorker(long workerId);

    List<Visit> findByUnregisteredClient(UnregisteredClient unregisteredClient);

    @Query(
            nativeQuery = true,
            value = "SELECT v.id, v.name, v.time_from, v.time_to, v.description, v.paid, "
                    + "v.deleted, v.visit_status_id, v.service_id, v.room_id, v.unregistered_client_id  FROM visit v"
                    + " INNER JOIN user_visit uv ON visit_id = v.id "
                    + " WHERE as_client = 0 AND uv.user_id = ?1 "
                    + " AND timestampdiff(MINUTE, time_from, ?2) <= 0 AND timestampdiff(MINUTE, time_to, ?3) >= 0 "
                    + " AND (v.visit_status_id = 1 OR v.visit_status_id = 2) "
                    + " AND v.deleted = 0 "
                    + " ORDER BY v.time_to asc "
    )
    List<Visit> findAllNotCancelledWhereUserIsWorkerBetween(Long userId, Date startTime, Date endTime);

    @Query(
            nativeQuery = true,
            value = "SELECT v.id, v.name, v.time_from, v.time_to, v.description, v.paid, "
                    + "v.deleted, v.visit_status_id, v.service_id, v.room_id, v.unregistered_client_id FROM visit v "
                    + "INNER JOIN user_visit uv ON visit_id = v.id "
                    + "WHERE as_client = 1 AND uv.user_id = ?1 "
                    + "AND timestampdiff(MINUTE, time_from, ?2) <= 0 AND timestampdiff(MINUTE, time_to, ?3) >= 0 "
                    + "AND (v.visit_status_id = 1 OR v.visit_status_id = 2) "
                    + " AND v.deleted = 0 "
                    + "ORDER BY v.time_to asc "
    )
    List<Visit> findAllNotCancelledWhereUserIsClientBetween(Long userId, Date startTime, Date endTime);

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM visit v "
                    + "WHERE room_id = ?1 AND "
                    + "(v.visit_status_id = 1 OR v.visit_status_id = 2) AND "
                    + "timestampdiff(MINUTE, time_from, ?2) <= 0 AND timestampdiff(MINUTE, time_to, ?3) >= 0 "
                    + " AND v.deleted = 0 "
                    + "ORDER BY time_to ASC "
    )
    List<Visit> findAllNotCancelledWeeklyRoomVisits(Long roomId, Date startTime, Date endTime);

    @Query(
            nativeQuery = true,
            value = "SELECT v.id, v.name, v.time_from, v.time_to, v.description, v.paid, "
                    + "v.deleted, v.visit_status_id, v.service_id, v.room_id, v.unregistered_client_id FROM visit v "
                    + "INNER JOIN visit_equipment_item vi ON visit_id = v.id "
                    + "WHERE vi.equipment_item_id = ?1 "
                    + "AND timestampdiff(MINUTE, time_from, ?2) <= 0 AND timestampdiff(MINUTE, time_to, ?3) >= 0 "
                    + "AND (v.visit_status_id = 1 OR v.visit_status_id = 2) "
                    + " AND v.deleted = 0 "
                    + "ORDER BY v.time_to asc "
    )
    List<Visit> findAllNotCancelledWeeklyItemVisits(Long itemId, Date startTime, Date endTime);

    @Query("SELECT CASE WHEN count(v) > 0 THEN true ELSE false END "
                    + "FROM Visit v WHERE v.service.id = :serviceId "
                    + "AND v.timeFrom >= CURRENT_DATE"
    )
    boolean existsInTheFutureWith(@Param("serviceId") Long serviceId);
}
