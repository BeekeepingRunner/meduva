package com.szusta.meduva.service;

import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.User;
import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.model.schedule.Schedule;
import com.szusta.meduva.payload.Term;
import com.szusta.meduva.repository.RoomRepository;
import com.szusta.meduva.repository.equipment.EquipmentItemRepository;
import com.szusta.meduva.repository.schedule.EquipmentScheduleRepository;
import com.szusta.meduva.repository.schedule.RoomScheduleRepository;
import com.szusta.meduva.repository.schedule.WorkerScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ScheduleChecker {

    private EquipmentItemRepository equipmentItemRepository;
    private RoomRepository roomRepository;
    private UserService userService;

    private EquipmentScheduleRepository equipmentScheduleRepository;
    private RoomScheduleRepository roomScheduleRepository;
    private WorkerScheduleRepository workerScheduleRepository;

    @Autowired
    public ScheduleChecker(EquipmentItemRepository equipmentItemRepository,
                           RoomRepository roomRepository,
                           UserService userService,
                           EquipmentScheduleRepository equipmentScheduleRepository,
                           RoomScheduleRepository roomScheduleRepository,
                           WorkerScheduleRepository workerScheduleRepository) {
        this.equipmentItemRepository = equipmentItemRepository;
        this.roomRepository = roomRepository;
        this.userService = userService;
        this.equipmentScheduleRepository = equipmentScheduleRepository;
        this.roomScheduleRepository = roomScheduleRepository;
        this.workerScheduleRepository = workerScheduleRepository;
    }

    public Optional<Term> getTermForCurrentWorker(Service service, List<EquipmentItem> suitableEqItems, Calendar currentlyCheckedTime) {

        Date currentCheckStart = currentlyCheckedTime.getTime();
        Date currentCheckEnd = getIntervalEnd(currentlyCheckedTime, service.getDurationInMin());

        Long workerId = userService.getCurrentUserId();
        User worker = userService.getUser(workerId);

        if (isWorkerFree(currentCheckStart, currentCheckEnd, worker)) {

            // get first available eqItem
            EquipmentItem availableEqItem = new EquipmentItem();
            for (EquipmentItem suitableItem : suitableEqItems) {
                if (isEqItemFree(currentCheckStart, currentCheckEnd, suitableItem)) {
                    availableEqItem = suitableItem;
                    break;
                }
            }

            // create term
            Term term = new Term(currentCheckStart, currentCheckEnd);
            term.setServiceName(service.getName());
            term.setWorkerName(worker.getName() + " " + worker.getSurname());
            term.setRoomName(availableEqItem.getRoom().getName());
            term.setEqItemName(availableEqItem.getName());

            return Optional.of(term);
        } else {
            return Optional.empty();
        }
    }

    private Date getIntervalEnd(Calendar currentlyCheckedTime, int serviceDurationInMinutes) {
        Calendar temp = (Calendar) currentlyCheckedTime.clone();
        temp.add(Calendar.MINUTE, serviceDurationInMinutes);
        return temp.getTime();
    }

    public boolean isWorkerFree(Date start, Date end, User worker) {
        List<Schedule> existingWorkerEvents =
                (List<Schedule>) workerScheduleRepository.findAnyBetween(start, end, worker.getId());
        return existingWorkerEvents.isEmpty();
    }

    public boolean isEqItemFree(Date start, Date end, EquipmentItem suitableItem) {
        List<Schedule> existingItemEvents =
                (List<Schedule>) equipmentScheduleRepository.findAnyBetween(start, end, suitableItem.getId());
        return existingItemEvents.isEmpty();
    }
}
