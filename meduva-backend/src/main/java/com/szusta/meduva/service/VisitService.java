package com.szusta.meduva.service;

import com.szusta.meduva.model.Service;
import com.szusta.meduva.payload.Term;
import com.szusta.meduva.repository.schedule.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@org.springframework.stereotype.Service
public class VisitService {

    private VisitRepository visitRepository;
    private ServicesService servicesService;

    @Autowired
    public VisitService(VisitRepository visitRepository,
                        ServicesService servicesService) {
        this.visitRepository = visitRepository;
        this.servicesService = servicesService;
    }

    public List<Term> getTermsForService(Long serviceId) {

        Service service = servicesService.findById(serviceId);
        int serviceDurationInMinutes = service.getDurationInMin();
        System.out.println(serviceDurationInMinutes);

        // === get first potential term (YYYY-MM-DD-hh-mm-ss)
        // and start checking subsequent terms ending at (00:00:00 on the day 30 days after now)
        // (30-min intervals)

        // get current time
        Calendar now = Calendar.getInstance();

        // round it to the nearest half-hour interval to start checking from it
        Calendar currentlyCheckedTime = (Calendar) now.clone();
        int currMinutes = currentlyCheckedTime.get(Calendar.MINUTE);
        int mod = currMinutes % 30;
        currentlyCheckedTime.add(Calendar.MINUTE, mod < 15 ? -mod : (30 - mod));
        currentlyCheckedTime.set(Calendar.SECOND, 0);
        currentlyCheckedTime.set(Calendar.MILLISECOND, 0);

        do {
            // get time interval to check
            Date currentCheckStart = currentlyCheckedTime.getTime();
            Calendar temp = (Calendar) currentlyCheckedTime.clone();
            temp.add(Calendar.MINUTE, serviceDurationInMinutes);
            Date currentCheckEnd = temp.getTime();

            // check if there are any room and equipment that is free in that interval
            System.out.println(currentCheckStart);
            System.out.println(currentCheckEnd);
            System.out.println();

            // proceed to the next interval
            currentlyCheckedTime.add(Calendar.MINUTE, 30);

        } while (!hasThirtyDaysPassedBetween(now, currentlyCheckedTime));

        return new ArrayList<>();
    }

    private boolean hasThirtyDaysPassedBetween(Calendar now, Calendar someday) {
        Calendar tempSomeday = (Calendar) someday.clone();
        tempSomeday.add(Calendar.DAY_OF_MONTH, -30);
        return now.before(tempSomeday);
    }
}
