package com.szusta.meduva.service;

import com.szusta.meduva.exception.EntityRecordNotFoundException;
import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.payload.Term;
import com.szusta.meduva.repository.RoomRepository;
import com.szusta.meduva.repository.ServiceRepository;
import com.szusta.meduva.repository.equipment.EquipmentItemRepository;
import com.szusta.meduva.repository.schedule.VisitRepository;
import com.szusta.meduva.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@org.springframework.stereotype.Service
public class VisitService {

    private VisitRepository visitRepository;
    private ServiceRepository serviceRepository;
    private EquipmentItemRepository itemRepository;
    private RoomRepository roomRepository;
    private ScheduleChecker scheduleChecker;

    @Autowired
    public VisitService(VisitRepository visitRepository,
                        ServiceRepository serviceRepository,
                        EquipmentItemRepository itemRepository,
                        RoomRepository roomRepository,
                        ScheduleChecker scheduleChecker) {
        this.visitRepository = visitRepository;
        this.serviceRepository = serviceRepository;
        this.itemRepository = itemRepository;
        this.roomRepository = roomRepository;
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

        Calendar now = Calendar.getInstance();
        // temp for testing ===============
        now.add(Calendar.MONTH, 1);
        //=================================
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

    private boolean doesRequiredEquipmentExist(Long serviceId) {
        List<EquipmentItem> suitableEqItems = itemRepository.findAllSuitableForService(serviceId);
        return !suitableEqItems.isEmpty();
    }
}
