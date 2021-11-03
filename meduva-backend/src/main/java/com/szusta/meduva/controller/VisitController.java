package com.szusta.meduva.controller;

import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.User;
import com.szusta.meduva.model.schedule.Visit;
import com.szusta.meduva.payload.Term;
import com.szusta.meduva.service.ServicesService;
import com.szusta.meduva.service.user.UserService;
import com.szusta.meduva.service.visit.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/visit")
public class VisitController {

    VisitService visitService;
    UserService userService;
    ServicesService servicesService;

    @Autowired
    public VisitController(VisitService visitService,
                           UserService userService,
                           ServicesService servicesService) {
        this.visitService = visitService;
        this.userService = userService;
        this.servicesService = servicesService;
    }

    @GetMapping("/terms-for-service/{serviceId}")
    public List<Term> getCurrentWorkerTermsForService(@PathVariable Long serviceId) {
        User worker = userService.findById(userService.getCurrentUserId());
        Service service = servicesService.findById(serviceId);
        return visitService.getTermsForWorker(worker, service);
    }

    @GetMapping("/get-available-days-in-month")
    public List<Date> getAvailableDaysInMonth(@RequestParam Long workerId, @RequestParam Long serviceId, @RequestParam String dayDate) throws ParseException {
        List<Date> availableDays =  new ArrayList<>();
        Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dayDate);
        availableDays.add(date);
        return availableDays;
    }

    @PostMapping
    public ResponseEntity<Visit> saveNewVisit(@RequestBody Term term) {
        return ResponseEntity.of(visitService.saveNewVisit(term));
    }
}
