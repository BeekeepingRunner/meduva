package com.szusta.meduva.repository.schedule.visit;

import com.szusta.meduva.model.schedule.visit.VisitStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitStatusRepository extends JpaRepository<VisitStatus, Long> {

}
