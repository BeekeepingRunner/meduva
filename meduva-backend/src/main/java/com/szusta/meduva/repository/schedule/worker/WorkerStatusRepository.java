package com.szusta.meduva.repository.schedule.worker;

import com.szusta.meduva.model.schedule.status.WorkerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerStatusRepository extends JpaRepository<WorkerStatus, Long> {

}
