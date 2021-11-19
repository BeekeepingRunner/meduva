package com.szusta.meduva.repository.schedule.worker;

import com.szusta.meduva.model.schedule.WorkerSchedule;
import com.szusta.meduva.repository.undeletable.UndeletableRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WorkerScheduleRepository extends UndeletableRepository<WorkerSchedule> {

    @Query(
            value =
                    "select * from worker_schedule ws "
                            +   "where ws.worker_id = ?3 and ("
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
    List<WorkerSchedule> findAllDuring(Date startTime, Date endTime, long workerId);

    @Query(
            value =
                    "select * from worker_schedule ws "
                            +   "where ws.worker_id = ?3 and "
                            +   "ws.worker_status_id = ?4 and ("
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
    List<WorkerSchedule> findAllDuring(Date startTime, Date endTime, long workerId, Long workerStatusId);

    @Modifying
    @Query(
            nativeQuery = true,
            value = "DELETE FROM worker_schedule wh WHERE wh.worker_id = ?1 AND "
                    + " timestampdiff(MINUTE, time_from, ?2) <= 0 AND timestampdiff(MINUTE, time_to, ?3) >= 0"
    )
    void deleteByWorkerIdBetween(Long workerId, Date start, Date end);

    @Modifying
    @Query(
            nativeQuery = true,
            value = "DELETE FROM worker_schedule wh WHERE wh.worker_id = ?1 AND "
                    + " wh.worker_status_id = ?2 AND"
                    + " timestampdiff(MINUTE, time_from, ?3) <= 0 AND timestampdiff(MINUTE, time_to, ?4) >= 0"
    )
    void deleteByWorkerIdBetween(Long workerId, Long workerStatusId, Date start, Date end);

    @Modifying
    @Query(
            nativeQuery = true,
            value = "DELETE FROM worker_schedule wh WHERE wh.worker_id = ?1 AND "
                    + " wh.time_from = ?2"
    )
    void deleteByWorkerIdWithTimeFrom(Long workerId, Date timeFrom);
}
