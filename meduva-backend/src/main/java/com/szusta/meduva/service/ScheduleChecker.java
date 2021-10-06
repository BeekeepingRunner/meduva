package com.szusta.meduva.service;

import com.szusta.meduva.model.Service;
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

        if (isWorkerFree(currentCheckStart, currentCheckEnd, workerId)) {
            // TODO: check for free equipment Items
            // ..
            Term term = new Term(currentCheckStart, currentCheckEnd);
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

    public boolean isWorkerFree(Date start, Date end, Long workerId) {
        List<Schedule> existingWorkerEvents =
                (List<Schedule>) workerScheduleRepository.findAnyBetween(start, end, workerId);
        return existingWorkerEvents.isEmpty();
    }
}
