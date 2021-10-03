package com.szusta.meduva.service;

import com.szusta.meduva.model.Service;
import com.szusta.meduva.payload.Term;
import com.szusta.meduva.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@org.springframework.stereotype.Service
public class VisitService {

    private VisitRepository visitRepository;
    private ServicesService servicesService;

    @Autowired
    public VisitService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    public List<Term> getTermsForService(Long serviceId) {

        Service service = servicesService.findById(serviceId);
        int serviceDurationInMinutes = service.getDurationInMin();

        Calendar calendar = Calendar.getInstance();

        // 1. get first potential term (YYYY-MM-DD-hh-mm-ss) - 00:00:00 on the first day of the current week
        // and start checking subsequent terms ending at (00:00:00 on the first day of the current week - durationInMinutes)
        // (30-min intervals)

        // 1.1 get appropriate datetime

        // 1.2 check if there are any room and equipment that is free on the interval that starts at previously mentioned
        // datetime and ends at (datetime + serviceDurationInMinutes).

        return new ArrayList<>();
    }
}
