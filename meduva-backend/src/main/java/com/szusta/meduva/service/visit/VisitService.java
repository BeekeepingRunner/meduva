package com.szusta.meduva.service.visit;

import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.User;
import com.szusta.meduva.model.schedule.Visit;
import com.szusta.meduva.payload.Term;
import com.szusta.meduva.repository.RoomRepository;
import com.szusta.meduva.repository.schedule.visit.VisitRepository;
import com.szusta.meduva.service.TermGenerator;
import com.szusta.meduva.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.time.ZoneId;
import java.util.*;

@org.springframework.stereotype.Service
public class VisitService {

    private VisitRepository visitRepository;
    private VisitBuilder visitBuilder;

    private RoomRepository roomRepository;

    private TermGenerator termGenerator;
    private VisitScheduleGenerator visitScheduleGenerator;

    @Autowired
    public VisitService(VisitRepository visitRepository,
                        VisitBuilder visitBuilder,
                        VisitScheduleGenerator visitScheduleGenerator,
                        RoomRepository roomRepository,
                        TermGenerator termGenerator) {
        this.visitRepository = visitRepository;
        this.visitBuilder = visitBuilder;
        this.visitScheduleGenerator = visitScheduleGenerator;
        this.roomRepository = roomRepository;
        this.termGenerator = termGenerator;
    }

    // Checks subsequent time-intervals in range of several days, starting from now.
    // Returns empty list if there are no available Terms.
    public List<Term> getTermsForWorker(User worker, Service service) {

        List<Room> suitableRooms = roomRepository.findAllSuitableForService(service.getId());
        if (suitableRooms.isEmpty()) {
            return Collections.emptyList();
        }

        Calendar now = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.systemDefault()));
        Calendar currentlyCheckedTime = TimeUtils.roundToNextHalfHour(now);

        // check subsequent terms starting from now
        List<Term> possibleTerms = new ArrayList<>();
        do {
            Optional<Term> term = termGenerator.getTermForWorker(worker, service, suitableRooms, currentlyCheckedTime);
            term.ifPresent(possibleTerms::add);

            // proceed to the next interval
            // TODO: move forward until its worker work time
            currentlyCheckedTime.add(Calendar.MINUTE, TimeUtils.MINUTE_OFFSET);

        } while (!TimeUtils.isNDaysBetween(now, currentlyCheckedTime, 30));

        return possibleTerms;
    }

    public List<Date> getAvailableDaysOfMonth(User worker, Service service, Date anyDayOfMonth) {
        checkIfCanPerform(worker, service);
        List<Room> suitableRooms = getSuitableRooms(service);

        List<Date> availableDaysOfMonth = new ArrayList<>();

        Date monthStart = TimeUtils.getMonthStart(anyDayOfMonth);
        Date monthEnd = TimeUtils.getMonthEnd(anyDayOfMonth);

        return availableDaysOfMonth;
    }

    private void checkIfCanPerform(User worker, Service service) {
        if (!worker.canPerform(service)) {
            throw new RuntimeException("Cannot find available month days for visit: given worker cannot perform given service");
        }
    }

    private List<Room> getSuitableRooms(Service service) {
        List<Room> suitableRooms = roomRepository.findAllSuitableForService(service.getId());
        if (suitableRooms.isEmpty()) {
            throw new RuntimeException("Cannot find available days of month: no room suitable for a given service exist");
        } else {
            return suitableRooms;
        }
    }

    @Transactional
    public Optional<Visit> saveNewVisit(Term term) {
        Visit visit = visitBuilder.buildVisit(term);
        visitScheduleGenerator.generateVisitSchedules(visit);
        return Optional.of(visitRepository.save(visit));
    }
}
