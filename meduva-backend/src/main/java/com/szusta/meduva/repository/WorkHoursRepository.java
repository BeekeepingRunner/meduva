package com.szusta.meduva.repository;

import com.szusta.meduva.model.WorkHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WorkHoursRepository extends JpaRepository<WorkHours, Long> {

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM work_hours wh WHERE wh.worker_id = ?1 AND "
                    + " timestampdiff(MINUTE, start_time, ?2) <= 0 AND timestampdiff(MINUTE, end_time, ?3) >= 0"
    )
    List<WorkHours> getAllByWorkerIdBetween(Long workerId, Date start, Date end);

    @Modifying
    @Query(
            nativeQuery = true,
            value = "DELETE FROM work_hours wh WHERE wh.worker_id = ?1 AND "
                    + " timestampdiff(MINUTE, start_time, ?2) <= 0 AND timestampdiff(MINUTE, end_time, ?3) >= 0"
    )
    void deleteByWorkerIdBetween(Long workerId, Date start, Date end);
}
