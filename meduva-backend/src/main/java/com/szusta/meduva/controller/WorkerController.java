package com.szusta.meduva.controller;

import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.User;
import com.szusta.meduva.model.WorkHours;
import com.szusta.meduva.payload.WorkHoursPayload;
import com.szusta.meduva.service.WorkManager;
import com.szusta.meduva.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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

    @PostMapping("/set-work-hours/{workerId}")
    public WorkHours setWorkHours(@PathVariable Long workerId, @RequestBody WorkHoursPayload workHoursPayload) {
        User worker = userService.findById(workerId);
        Date newWorkStartTime = workHoursPayload.getStartTime();
        Date newWorkEndTime = workHoursPayload.getEndTime();
        return workManager.setWorkHours(worker, newWorkStartTime, newWorkEndTime);
    }
}