package com.szusta.meduva.service.visit;

import com.szusta.meduva.exception.EntityRecordNotFoundException;
import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.User;
import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.model.schedule.Visit;
import com.szusta.meduva.model.schedule.status.VisitStatus;
import com.szusta.meduva.model.schedule.status.enums.EVisitStatus;
import com.szusta.meduva.payload.Term;
import com.szusta.meduva.repository.RoomRepository;
import com.szusta.meduva.repository.ServiceRepository;
import com.szusta.meduva.repository.UserRepository;
import com.szusta.meduva.repository.equipment.EquipmentItemRepository;
import com.szusta.meduva.repository.schedule.visit.VisitStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class VisitBuilder {

    private VisitStatusRepository visitStatusRepository;
    private ServiceRepository serviceRepository;
    private RoomRepository roomRepository;
    private UserRepository userRepository;
    private EquipmentItemRepository itemRepository;

    @Autowired
    public VisitBuilder(VisitStatusRepository visitStatusRepository,
                        ServiceRepository serviceRepository,
                        RoomRepository roomRepository,
                        UserRepository userRepository,
                        EquipmentItemRepository itemRepository) {
        this.visitStatusRepository = visitStatusRepository;
        this.serviceRepository = serviceRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    public Visit buildVisit(Term term) {

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
