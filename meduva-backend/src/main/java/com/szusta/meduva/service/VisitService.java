package com.szusta.meduva.service;

import com.szusta.meduva.exception.EntityRecordNotFoundException;
import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.User;
import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.model.schedule.visit.EVisitStatus;
import com.szusta.meduva.model.schedule.visit.Visit;
import com.szusta.meduva.model.schedule.visit.VisitStatus;
import com.szusta.meduva.payload.Term;
import com.szusta.meduva.repository.RoomRepository;
import com.szusta.meduva.repository.ServiceRepository;
import com.szusta.meduva.repository.UserRepository;
import com.szusta.meduva.repository.equipment.EquipmentItemRepository;
import com.szusta.meduva.repository.schedule.visit.VisitRepository;
import com.szusta.meduva.repository.schedule.visit.VisitStatusRepository;
import com.szusta.meduva.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.time.ZoneId;
import java.util.*;

@org.springframework.stereotype.Service
public class VisitService {

    private VisitRepository visitRepository;
    private VisitStatusRepository visitStatusRepository;
    private ServiceRepository serviceRepository;
    private EquipmentItemRepository itemRepository;
    private RoomRepository roomRepository;
    private UserRepository userRepository;

    private ScheduleChecker scheduleChecker;

    @Autowired
    public VisitService(VisitRepository visitRepository,
                        VisitStatusRepository visitStatusRepository,
                        ServiceRepository serviceRepository,
                        EquipmentItemRepository itemRepository,
                        RoomRepository roomRepository,
                        UserRepository userRepository,
                        ScheduleChecker scheduleChecker) {
        this.visitRepository = visitRepository;
        this.visitStatusRepository = visitStatusRepository;
        this.serviceRepository = serviceRepository;
        this.itemRepository = itemRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.scheduleChecker = scheduleChecker;
    }

    // Checks subsequent time-intervals in range of several days, starting from now.
    // Returns empty list if there are no available Terms.
    public List<Term> getTermsForCurrentWorker(Long serviceId) {

        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new EntityRecordNotFoundException("Service not found with id : " + serviceId));

        List<Room> suitableRooms = roomRepository.findAllSuitableForService(serviceId);
        if (suitableRooms.isEmpty()) {
            return Collections.emptyList();
        }

        Calendar now = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.systemDefault()));
        Calendar currentlyCheckedTime = TimeUtils.roundToNextHalfHour(now);

        // check subsequent terms starting from now
        List<Term> possibleTerms = new ArrayList<>();
        do {
            Optional<Term> term = scheduleChecker.getTermForCurrentWorker(service, suitableRooms, currentlyCheckedTime);
            term.ifPresent(possibleTerms::add);

            // proceed to the next interval
            currentlyCheckedTime.add(Calendar.MINUTE, TimeUtils.MINUTE_OFFSET);

        } while (!TimeUtils.hasNDaysPassedBetween(now, currentlyCheckedTime, 30));

        return possibleTerms;
    }

    @Transactional
    public Optional<Visit> saveNewVisit(Term term) {
        Visit visit = new Visit(term.getStartTime(), term.getEndTime());

        VisitStatus booked = visitStatusRepository.getById(EVisitStatus.VISIT_BOOKED.getValue());
        visit.setVisitStatus(booked);

        Service service = serviceRepository.findById(term.getServiceId())
                .orElseThrow(() -> new EntityRecordNotFoundException("Service from term not found in DB"));
        visit.setService(service);

        Room room = roomRepository.findById(term.getRoomId())
                .orElseThrow(() -> new EntityRecordNotFoundException("Room from term not found in DB"));
        visit.setRoom(room);

        User worker = userRepository.findById(term.getWorkerId())
                .orElseThrow(() -> new EntityRecordNotFoundException("Worker from term not found in DB (id : " + term.getWorkerId() + ")"));
        User client = userRepository.findById(term.getClientId())
                .orElseThrow(() -> new EntityRecordNotFoundException("Client from term not found in DB (id : " + term.getClientId() + ")"));
        List<User> visitUsers = new ArrayList<>();
        visitUsers.add(worker);
        visitUsers.add(client);
        visit.setUsers(visitUsers);

        if (term.getEqItemId() != null) {
            EquipmentItem eqItem = itemRepository.findById(term.getEqItemId())
                            .orElseThrow(() -> new EntityRecordNotFoundException("Item from term not found in DB (id : " + term.getEqItemId() + ")"));
            visit.setEqItems(Collections.singletonList(eqItem));
        }

        return Optional.of(visitRepository.save(visit));
    }
}
