package com.szusta.meduva.controller;

import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.User;
import com.szusta.meduva.model.WorkHours;
import com.szusta.meduva.model.schedule.WorkerSchedule;
import com.szusta.meduva.payload.TimeRange;
import com.szusta.meduva.payload.WeekBoundaries;
import com.szusta.meduva.service.WorkManager;
import com.szusta.meduva.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/worker")
public class WorkerController {

    UserService userService;
    WorkManager workManager;

    @Autowired
    public WorkerController(UserService userService,
                            WorkManager workManager) {
        this.userService = userService;
        this.workManager = workManager;
    }

    @GetMapping("/workerServices/{id}")
    public Service[] getWorkerServices(@PathVariable Long id) {
        return workManager.getWorkerServices(id);
    }

    @PostMapping("/assignServicesToWorker/{id}")
    public User assignServicesToWorker(@PathVariable Long id, @RequestBody Long[] request){
        return workManager.assignServicesToWorker(id, request);
    }

    @PostMapping("/get-week-work-hours/{workerId}")
    public List<WorkHours> getWeekWorkHours(@PathVariable Long workerId, @RequestBody WeekBoundaries weekBoundaries) {
        User worker = userService.findById(workerId);
        Date firstWeekDay = weekBoundaries.getFirstWeekDay();
        Date lastWeekDay = weekBoundaries.getLastWeekDay();
        return workManager.getWeeklyWorkHours(worker, firstWeekDay, lastWeekDay);
    }

    // TODO
    @PostMapping("/get-week-off-work-hours/{workerId}")
    public List<TimeRange> getWeeklyOffWorkHours(@PathVariable Long workerId, @RequestBody WeekBoundaries weekBoundaries) {
        User worker = userService.findById(workerId);
        Date firstWeekDay = weekBoundaries.getFirstWeekDay();
        Date lastWeekDay = weekBoundaries.getLastWeekDay();
        return workManager.getWeeklyOffWorkHours(worker, firstWeekDay, lastWeekDay);
    }

    @PostMapping("/get-week-absence-hours/{workerId}")
    public List<? super WorkerSchedule> getWeeklyAbsenceHours(@PathVariable Long workerId, @RequestBody WeekBoundaries weekBoundaries){
        User worker = userService.findById(workerId);
        Date firstWeekDay = weekBoundaries.getFirstWeekDay();
        Date lastWeekDay = weekBoundaries.getLastWeekDay();
        return workManager.getWeeklyAbsenceHours(worker, firstWeekDay, lastWeekDay);
    }

    @PostMapping("/set-work-hours/{workerId}")
    public WorkHours setWorkHours(@PathVariable Long workerId, @RequestBody TimeRange newWorkHours) {
        User worker = userService.findById(workerId);
        Date newWorkStartTime = newWorkHours.getStartTime();
        Date newWorkEndTime = newWorkHours.getEndTime();
        return workManager.setWorkHours(worker, newWorkStartTime, newWorkEndTime);
    }

    @PostMapping("/set-absence-hours/{workerId}")
    public WorkerSchedule setAbsenceHours(@PathVariable Long workerId, @RequestBody TimeRange absenceHours) {
        User worker = userService.findById(workerId);
        Date newAbsenceStartTime = absenceHours.getStartTime();
        Date newAbsenceEndTime = absenceHours.getEndTime();
        return workManager.setDailyAbsenceHours(worker, newAbsenceStartTime, newAbsenceEndTime);
    }
}