package com.szusta.meduva.service.visit;

import com.szusta.meduva.exception.EntityRecordNotFoundException;
import com.szusta.meduva.model.AccountlessClient;
import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.User;
import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.model.schedule.status.VisitStatus;
import com.szusta.meduva.model.schedule.status.enums.EVisitStatus;
import com.szusta.meduva.model.schedule.visit.UserVisit;
import com.szusta.meduva.model.schedule.visit.Visit;
import com.szusta.meduva.payload.Term;
import com.szusta.meduva.repository.AccountlessClientRepository;
import com.szusta.meduva.repository.RoomRepository;
import com.szusta.meduva.repository.ServiceRepository;
import com.szusta.meduva.repository.UserRepository;
import com.szusta.meduva.repository.equipment.EquipmentItemRepository;
import com.szusta.meduva.repository.schedule.visit.VisitRepository;
import com.szusta.meduva.repository.schedule.visit.VisitStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.Collections;

@org.springframework.stereotype.Service
public class VisitBuilder {

    private VisitRepository visitRepository;
    private AccountlessClientRepository accountlessClientRepository;
    private VisitStatusRepository visitStatusRepository;
    private ServiceRepository serviceRepository;
    private RoomRepository roomRepository;
    private UserRepository userRepository;
    private EquipmentItemRepository itemRepository;

    @Autowired
    public VisitBuilder(VisitRepository visitRepository,
                        AccountlessClientRepository accountlessClientRepository,
                        VisitStatusRepository visitStatusRepository,
                        ServiceRepository serviceRepository,
                        RoomRepository roomRepository,
                        UserRepository userRepository,
                        EquipmentItemRepository itemRepository) {
        this.visitRepository = visitRepository;
        this.accountlessClientRepository = accountlessClientRepository;
        this.visitStatusRepository = visitStatusRepository;
        this.serviceRepository = serviceRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public Visit buildVisit(Term term) {
        Visit visit;
        if (term.isClientUnregistered()) {
            visit = createWithUnregisteredClient(term);
        } else {
            visit = createStandardVisit(term);
        }

        visit = setAdditionalVisitData(visit, term);

        return visitRepository.save(visit);
    }

    private Visit createWithUnregisteredClient(Term term) {
        User worker = userRepository.findById(term.getWorkerId())
                .orElseThrow(() -> new EntityRecordNotFoundException("Worker not found in DB with id = " + term.getWorkerId()));
        Visit visit = new Visit(
                term.getStartTime(),
                term.getEndTime(),
                new UserVisit(worker, false));

        AccountlessClient accountlessClient = accountlessClientRepository.findById(term.getClientId())
                .orElseThrow(() -> new RuntimeException("Cannot find accountless client in db with id : " + term.getClientId()));
        visit.setUnregisteredClient(accountlessClient);
        return visit;
    }

    private Visit createStandardVisit(Term term) {
        User worker = userRepository.findById(term.getWorkerId())
                .orElseThrow(() -> new EntityRecordNotFoundException("Worker not found in DB with id = " + term.getWorkerId()));
        User client = userRepository.findById(term.getClientId())
                .orElseThrow(() -> new EntityRecordNotFoundException("Client not found in DB with id = " + term.getClientId()));
        return new Visit(
                term.getStartTime(),
                term.getEndTime(),
                new UserVisit(worker, false),
                new UserVisit(client, true));
    }

    private Visit setAdditionalVisitData(Visit visit, Term term) {
        VisitStatus booked = visitStatusRepository.findById(EVisitStatus.VISIT_BOOKED.getValue())
                .orElseThrow(() -> new EntityRecordNotFoundException("Visit status not found in DB with id = " + EVisitStatus.VISIT_BOOKED.getValue()));
        Service service = serviceRepository.findById(term.getServiceId())
                .orElseThrow(() -> new EntityRecordNotFoundException("Service not found in DB with id = " + term.getServiceId()));
        Room room = roomRepository.findById(term.getRoomId())
                .orElseThrow(() -> new EntityRecordNotFoundException("Room not found in DB with id = " + term.getRoomId()));

        visit.setVisitStatus(booked);
        visit.setService(service);
        visit.setRoom(room);
        visit.setDescription(term.getDescription());
        if (term.getEqItemId() != null) {
            EquipmentItem eqItem = itemRepository.findById(term.getEqItemId())
                    .orElseThrow(() -> new EntityRecordNotFoundException("Equipment item not found in DB with id = " + term.getEqItemId()));
            visit.setEqItems(Collections.singletonList(eqItem));
        }
        return visit;
    }
}
