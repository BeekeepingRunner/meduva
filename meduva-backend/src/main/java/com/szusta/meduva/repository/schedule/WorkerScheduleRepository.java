package com.szusta.meduva.repository.schedule;

import com.szusta.meduva.model.schedule.WorkerSchedule;
import com.szusta.meduva.repository.undeletable.UndeletableRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerScheduleRepository extends UndeletableRepository<WorkerSchedule> {

}
