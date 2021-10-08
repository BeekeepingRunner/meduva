package com.szusta.meduva.controller;

import com.szusta.meduva.payload.Term;
import com.szusta.meduva.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void saveNewVisit(@RequestBody Term term) {
        visitService.saveNewVisit(term);
    }
}
