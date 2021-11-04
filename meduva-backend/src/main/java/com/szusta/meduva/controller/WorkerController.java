package com.szusta.meduva.controller;

import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.User;
import com.szusta.meduva.model.WorkHours;
import com.szusta.meduva.payload.TimeRange;
import com.szusta.meduva.payload.WeekBoundaries;
import com.szusta.meduva.service.ServicesService;
import com.szusta.meduva.service.WorkManager;
import com.szusta.meduva.service.WorkerService;
import com.szusta.meduva.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/worker")
public class WorkerController {

    UserService userService;
    WorkerService workerService;
    ServicesService servicesService;
    WorkManager workManager;

    @Autowired
    public WorkerController(UserService userService,
                            WorkerService workerService,
                            ServicesService servicesService,
                            WorkManager workManager) {
        this.userService = userService;
        this.workerService = workerService;
        this.servicesService = servicesService;
        this.workManager = workManager;
    }

    @GetMapping("/find-by-service/{serviceId}")
    public List<User> findWorkersByService(@PathVariable Long serviceId) {
        Service service = servicesService.findById(serviceId);
        return this.workerService.findAllByService(service);
    }

    @GetMapping("/workerServices/{id}")
    public Service[] getWorkerServices(@PathVariable Long id) {
        User worker = userService.findById(id);
        return workerService.getWorkerServices(worker);
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

    @PostMapping("/set-work-hours/{workerId}")
    public WorkHours setWorkHours(@PathVariable Long workerId, @RequestBody TimeRange newWorkHours) {
        User worker = userService.findById(workerId);
        Date newWorkStartTime = newWorkHours.getStartTime();
        Date newWorkEndTime = newWorkHours.getEndTime();
        return workManager.setWorkHours(worker, newWorkStartTime, newWorkEndTime);
    }
}