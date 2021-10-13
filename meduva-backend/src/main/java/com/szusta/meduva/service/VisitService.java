package com.szusta.meduva.service;

import com.szusta.meduva.exception.EntityRecordNotFoundException;
import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.User;
import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.model.schedule.EquipmentSchedule;
import com.szusta.meduva.model.schedule.RoomSchedule;
import com.szusta.meduva.model.schedule.WorkerSchedule;
import com.szusta.meduva.model.schedule.status.EquipmentStatus;
import com.szusta.meduva.model.schedule.status.RoomStatus;
import com.szusta.meduva.model.schedule.status.WorkerStatus;
import com.szusta.meduva.model.schedule.status.enums.EEquipmentStatus;
import com.szusta.meduva.model.schedule.status.enums.ERoomStatus;
import com.szusta.meduva.model.schedule.status.enums.EVisitStatus;
import com.szusta.meduva.model.schedule.Visit;
import com.szusta.meduva.model.schedule.status.VisitStatus;
import com.szusta.meduva.model.schedule.status.enums.EWorkerStatus;
import com.szusta.meduva.payload.Term;
import com.szusta.meduva.repository.RoomRepository;
import com.szusta.meduva.repository.ServiceRepository;
import com.szusta.meduva.repository.UserRepository;
import com.szusta.meduva.repository.equipment.EquipmentItemRepository;
import com.szusta.meduva.repository.schedule.equipment.EquipmentScheduleRepository;
import com.szusta.meduva.repository.schedule.equipment.EquipmentStatusRepository;
import com.szusta.meduva.repository.schedule.room.RoomScheduleRepository;
import com.szusta.meduva.repository.schedule.room.RoomStatusRepository;
import com.szusta.meduva.repository.schedule.worker.WorkerScheduleRepository;
import com.szusta.meduva.repository.schedule.visit.VisitRepository;
import com.szusta.meduva.repository.schedule.visit.VisitStatusRepository;
import com.szusta.meduva.repository.schedule.worker.WorkerStatusRepository;
import com.szusta.meduva.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.time.ZoneId;
import java.util.*;

@org.springframework.stereotype.Service
public class VisitService {

    private VisitRepository visitRepository;

    private VisitStatusRepository visitStatusRepository;
    private RoomStatusRepository roomStatusRepository;
    private EquipmentStatusRepository equipmentStatusRepository;
    private WorkerStatusRepository workerStatusRepository;

    private ServiceRepository serviceRepository;
    private EquipmentItemRepository itemRepository;
    private RoomRepository roomRepository;
    private UserRepository userRepository;

    private ScheduleChecker scheduleChecker;
    private RoomScheduleRepository roomScheduleRepository;
    private WorkerScheduleRepository workerScheduleRepository;
    private EquipmentScheduleRepository equipmentScheduleRepository;

    @Autowired
    public VisitService(VisitRepository visitRepository,
                        VisitStatusRepository visitStatusRepository,
                        RoomStatusRepository roomStatusRepository,
                        EquipmentStatusRepository equipmentStatusRepository,
                        WorkerStatusRepository workerStatusRepository,
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
        this.roomStatusRepository = roomStatusRepository;
        this.equipmentStatusRepository = equipmentStatusRepository;
        this.workerStatusRepository = workerStatusRepository;
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
        RoomStatus roomOccupied = roomStatusRepository.getById(ERoomStatus.ROOM_OCCUPIED.getValue());
        roomSchedule.setRoomStatus(roomOccupied);
        roomScheduleRepository.save(roomSchedule);

        User worker = userRepository.getById(term.getWorkerId());
        WorkerStatus workerOccupied = workerStatusRepository.getById(EWorkerStatus.WORKER_OCCUPIED.getValue());
        WorkerSchedule workerSchedule = new WorkerSchedule(worker, term.getStartTime(), term.getEndTime());
        workerSchedule.setWorkerStatus(workerOccupied);
        workerScheduleRepository.save(workerSchedule);

        // TODO: check itemless flag
        if (visit.getEqItems() != null) {
            EquipmentSchedule equipmentSchedule = new EquipmentSchedule(visit.getEqItems().get(0), term.getStartTime(), term.getEndTime());
            EquipmentStatus equipmentOccupied = equipmentStatusRepository.getById(EEquipmentStatus.EQUIPMENT_OCCUPIED.getValue());
            equipmentSchedule.setEquipmentStatus(equipmentOccupied);
            equipmentScheduleRepository.save(equipmentSchedule);
        }

        return Optional.of(visitRepository.save(visit));
    }

    private Visit buildVisit(Term term) {

        Visit visit = new Visit(term.getStartTime(), term.getEndTime());

        VisitStatus booked = visitStatusRepository.findById(EVisitStatus.VISIT_BOOKED.getValue())
                .orElseThrow(() -> new EntityRecordNotFoundException("Visit status not found in DB with id = " + EVisitStatus.VISIT_BOOKED.getValue()));
        Service service = serviceRepository.findById(term.getServiceId())
                .orElseThrow(() -> new EntityRecordNotFoundException("Service not found in DB with id = " + term.getServiceId()));
        Room room = roomRepository.findById(term.getRoomId())
                .orElseThrow(() -> new EntityRecordNotFoundException("Room not found in DB with id = " + term.getRoomId()));
        User worker = userRepository.findById(term.getWorkerId())
                .orElseThrow(() -> new EntityRecordNotFoundException("Worker not found in DB with id = " + term.getWorkerId()));
        User client = userRepository.findById(term.getClientId())
                .orElseThrow(() -> new EntityRecordNotFoundException("Client not found in DB with id = " + term.getClientId()));

        visit.setVisitStatus(booked);
        visit.setService(service);
        visit.setRoom(room);

        List<User> visitUsers = new ArrayList<>();
        visitUsers.add(worker);
        visitUsers.add(client);
        visit.setUsers(visitUsers);

        if (term.getEqItemId() != null) {
            EquipmentItem eqItem = itemRepository.findById(term.getEqItemId())
                    .orElseThrow(() -> new EntityRecordNotFoundException("Equipment item not found in DB with id = " + term.getEqItemId()));
            visit.setEqItems(Collections.singletonList(eqItem));
        }

        return visit;
    }
}
