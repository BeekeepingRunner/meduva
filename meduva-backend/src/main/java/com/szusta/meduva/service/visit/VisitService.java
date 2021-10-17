package com.szusta.meduva.service.visit;

import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.User;
import com.szusta.meduva.model.schedule.Visit;
import com.szusta.meduva.payload.Term;
import com.szusta.meduva.repository.RoomRepository;
import com.szusta.meduva.repository.schedule.visit.VisitRepository;
import com.szusta.meduva.service.ScheduleChecker;
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

    private ScheduleChecker scheduleChecker;
    private ScheduleGenerator scheduleGenerator;

    @Autowired
    public VisitService(VisitRepository visitRepository,
                        VisitBuilder visitBuilder,
                        ScheduleGenerator scheduleGenerator,
                        RoomRepository roomRepository,
                        ScheduleChecker scheduleChecker) {
        this.visitRepository = visitRepository;
        this.visitBuilder = visitBuilder;
        this.scheduleGenerator = scheduleGenerator;
        this.roomRepository = roomRepository;
        this.scheduleChecker = scheduleChecker;
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
            Optional<Term> term = scheduleChecker.getTermForWorker(worker, service, suitableRooms, currentlyCheckedTime);
            term.ifPresent(possibleTerms::add);

            // proceed to the next interval
            // TODO: move forward until its worker work time
            currentlyCheckedTime.add(Calendar.MINUTE, TimeUtils.MINUTE_OFFSET);

        } while (!TimeUtils.isNDaysBetween(now, currentlyCheckedTime, 30));

        return possibleTerms;
    }

    @Transactional
    public Optional<Visit> saveNewVisit(Term term) {
        Visit visit = visitBuilder.buildVisit(term);
        scheduleGenerator.generateSchedules(visit);
        return Optional.of(visitRepository.save(visit));
    }
}