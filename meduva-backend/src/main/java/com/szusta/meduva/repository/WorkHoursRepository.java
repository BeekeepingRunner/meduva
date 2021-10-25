package com.szusta.meduva.repository;

import com.szusta.meduva.model.WorkHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface WorkHoursRepository extends JpaRepository<WorkHours, Long> {

    @Modifying
    @Query(
            nativeQuery = true,
            value = "DELETE FROM work_hours wh WHERE wh.start_time BETWEEN ?1 AND ?2"
    )
    void deleteBetween(Date start, Date end);
}
