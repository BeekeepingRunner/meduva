package com.szusta.meduva.controller;

import com.szusta.meduva.model.schedule.visit.Visit;
import com.szusta.meduva.payload.Term;
import com.szusta.meduva.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visit")
public class VisitController {

    VisitService visitService;

    @Autowired
    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @GetMapping("/terms-for-service/{serviceId}")
    public List<Term> getTermsForService(@PathVariable Long serviceId) {
        return visitService.getTermsForCurrentWorker(serviceId);
    }

    @PostMapping
    public ResponseEntity<Visit> saveNewVisit(@RequestBody Term term) {
        return ResponseEntity.of(visitService.saveNewVisit(term));
    }
}
