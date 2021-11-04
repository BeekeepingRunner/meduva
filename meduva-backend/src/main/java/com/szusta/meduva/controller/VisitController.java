package com.szusta.meduva.controller;

import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.User;
import com.szusta.meduva.model.schedule.Visit;
import com.szusta.meduva.payload.Term;
import com.szusta.meduva.service.ScheduleChecker;
import com.szusta.meduva.service.ServicesService;
import com.szusta.meduva.service.user.UserService;
import com.szusta.meduva.service.visit.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/visit")
public class VisitController {

    VisitService visitService;
    UserService userService;
    ServicesService servicesService;
    ScheduleChecker scheduleChecker;

    @Autowired
    public VisitController(VisitService visitService,
                           UserService userService,
                           ServicesService servicesService,
                           ScheduleChecker scheduleChecker) {
        this.visitService = visitService;
        this.userService = userService;
        this.servicesService = servicesService;
        this.scheduleChecker = scheduleChecker;
    }

    @GetMapping("/terms-for-service/{serviceId}")
    public List<Term> getCurrentWorkerTermsForService(@PathVariable Long serviceId) {
        User worker = userService.findById(userService.getCurrentUserId());
        Service service = servicesService.findById(serviceId);
        return visitService.getTermsForWorker(worker, service);
    }

    @GetMapping("/get-worker-available-days-in-month")
    public List<Date> getAvailableDaysOfMonth(@RequestParam Long workerId, @RequestParam Long serviceId, @RequestParam String anyDayFromMonth) throws ParseException {

        Date anyDayOfMonth = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(anyDayFromMonth);
        User worker = userService.findById(workerId);
        Service service = servicesService.findById(serviceId);
        return scheduleChecker.getAvailableDaysOfMonth(worker, service, anyDayOfMonth);
    }

    @PostMapping
    public ResponseEntity<Visit> saveNewVisit(@RequestBody Term term) {
        return ResponseEntity.of(visitService.saveNewVisit(term));
    }
}
