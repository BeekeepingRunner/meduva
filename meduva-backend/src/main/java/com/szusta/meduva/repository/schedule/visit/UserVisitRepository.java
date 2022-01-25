package com.szusta.meduva.repository.schedule.visit;

import com.szusta.meduva.model.schedule.visit.UserVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVisitRepository extends JpaRepository<UserVisit, Long> {

}
