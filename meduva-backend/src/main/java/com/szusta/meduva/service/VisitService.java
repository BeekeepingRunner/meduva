package com.szusta.meduva.service;

import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.User;
import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.model.schedule.EquipmentSchedule;
import com.szusta.meduva.model.schedule.RoomSchedule;
import com.szusta.meduva.model.schedule.WorkerSchedule;
import com.szusta.meduva.model.schedule.visit.EVisitStatus;
import com.szusta.meduva.model.schedule.visit.Visit;
import com.szusta.meduva.model.schedule.visit.VisitStatus;
import com.szusta.meduva.payload.Term;
import com.szusta.meduva.repository.RoomRepository;
import com.szusta.meduva.repository.ServiceRepository;
import com.szusta.meduva.repository.UserRepository;
import com.szusta.meduva.repository.equipment.EquipmentItemRepository;
import com.szusta.meduva.repository.schedule.EquipmentScheduleRepository;
import com.szusta.meduva.repository.schedule.RoomScheduleRepository;
import com.szusta.meduva.repository.schedule.WorkerScheduleRepository;
import com.szusta.meduva.repository.schedule.visit.VisitRepository;
import com.szusta.meduva.repository.schedule.visit.VisitStatusRepository;
import com.szusta.meduva.util.TimeUtils;

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
    private RoomScheduleRepository roomScheduleRepository;
    private WorkerScheduleRepository workerScheduleRepository;
    private EquipmentScheduleRepository equipmentScheduleRepository;

    public VisitService(VisitRepository visitRepository,
                        VisitStatusRepository visitStatusRepository,
                        ServiceRepository serviceRepository,
                        EquipmentItemRepository itemRepository,
                        RoomRepository roomRepository,
                        UserRepository userRepository,
                        ScheduleChecker scheduleChecker,
                        RoomScheduleRepository roomScheduleRepository,
                        WorkerScheduleRepository workerScheduleRepository,
                        EquipmentScheduleRepository equipmentScheduleRepository) {
        this.visitRepository = visitRepository;
        this.visitStatusRepository = visitStatusRepository;
        this.serviceRepository = serviceRepository;
        this.itemRepository = itemRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.scheduleChecker = scheduleChecker;
        this.roomScheduleRepository = roomScheduleRepository;
        this.workerScheduleRepository = workerScheduleRepository;
        this.equipmentScheduleRepository = equipmentScheduleRepository;
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
        // TODO: validate all term info
        Visit visit = buildVisit(term);

        // TODO: add all appropriate schedule rows
        RoomSchedule roomSchedule = new RoomSchedule(visit.getRoom(), term.getStartTime(), term.getEndTime());
        roomScheduleRepository.save(roomSchedule);

        User worker = userRepository.getById(term.getWorkerId());
        WorkerSchedule workerSchedule = new WorkerSchedule(worker, term.getStartTime(), term.getEndTime());
        workerScheduleRepository.save(workerSchedule);

        // TODO: check itemless flag
        if (visit.getEqItems() != null) {
            EquipmentSchedule equipmentSchedule = new EquipmentSchedule(visit.getEqItems().get(0), term.getStartTime(), term.getEndTime());
            equipmentScheduleRepository.save(equipmentSchedule);
        }

        return Optional.of(visitRepository.save(visit));
    }

    private Visit buildVisit(Term term) {

        Visit visit = new Visit(term.getStartTime(), term.getEndTime());

        VisitStatus booked = visitStatusRepository.getById(EVisitStatus.VISIT_BOOKED.getValue());
        Service service = serviceRepository.getById(term.getServiceId());
        Room room = roomRepository.getById(term.getRoomId());
        User worker = userRepository.getById(term.getWorkerId());
        User client = userRepository.getById(term.getClientId());

        visit.setVisitStatus(booked);
        visit.setService(service);
        visit.setRoom(room);

        List<User> visitUsers = new ArrayList<>();
        visitUsers.add(worker);
        visitUsers.add(client);
        visit.setUsers(visitUsers);

        if (term.getEqItemId() != null) {
            EquipmentItem eqItem = itemRepository.getById(term.getEqItemId());
            visit.setEqItems(Collections.singletonList(eqItem));
        }

        return visit;
    }
}
