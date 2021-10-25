package com.szusta.meduva.repository;

import com.szusta.meduva.model.WorkHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkHoursRepository extends JpaRepository<WorkHours, Long> {

}
