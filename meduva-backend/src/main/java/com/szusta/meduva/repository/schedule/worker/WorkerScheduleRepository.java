package com.szusta.meduva.repository.schedule.worker;

import com.szusta.meduva.model.schedule.WorkerSchedule;
import com.szusta.meduva.repository.undeletable.UndeletableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerScheduleRepository extends UndeletableRepository<WorkerSchedule> {

    @Query(
        value =
            "select * from worker_schedule s "
        +   "where s.worker_id = ?3 and ("
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
    List<? super WorkerSchedule> findAnyBetween(Date start, Date end, Long workerId);

    Optional<WorkerSchedule> deleteWorkerScheduleByUserId(Long id);


}
