package com.szusta.meduva.service;

import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.User;
import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.model.equipment.EquipmentModel;
import com.szusta.meduva.model.schedule.Schedule;
import com.szusta.meduva.payload.Term;
import com.szusta.meduva.repository.equipment.EquipmentItemRepository;
import com.szusta.meduva.repository.equipment.EquipmentModelRepository;
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
    private EquipmentModelRepository equipmentModelRepository;
    private UserService userService;

    private EquipmentScheduleRepository equipmentScheduleRepository;
    private RoomScheduleRepository roomScheduleRepository;
    private WorkerScheduleRepository workerScheduleRepository;

    @Autowired
    public ScheduleChecker(EquipmentItemRepository equipmentItemRepository,
                           EquipmentModelRepository equipmentModelRepository,
                           UserService userService,
                           EquipmentScheduleRepository equipmentScheduleRepository,
                           RoomScheduleRepository roomScheduleRepository,
                           WorkerScheduleRepository workerScheduleRepository) {
        this.equipmentItemRepository = equipmentItemRepository;
        this.equipmentModelRepository = equipmentModelRepository;
        this.userService = userService;
        this.equipmentScheduleRepository = equipmentScheduleRepository;
        this.roomScheduleRepository = roomScheduleRepository;
        this.workerScheduleRepository = workerScheduleRepository;
    }

    public Optional<Term> getTermForWorker(User worker, Service service, List<Room> suitableRooms, Calendar currentlyCheckedTime) {

        Date currentCheckStart = currentlyCheckedTime.getTime();
        Date currentCheckEnd = getIntervalEnd(currentlyCheckedTime, service.getDurationInMin());

        if (!isWorkerFree(currentCheckStart, currentCheckEnd, worker)) {
            return Optional.empty();
        }
        // get first available room
        Optional<Room> availableRoom = getFirstAvailableRoom(suitableRooms, currentCheckStart, currentCheckEnd);
        if (availableRoom.isEmpty()) {
            return Optional.empty();
        }

        boolean itemless = isServiceItemless(service.getId());
        Optional<EquipmentItem> availableEqItem = Optional.empty();
        if (!itemless) {
            List<EquipmentItem> suitableEqItems =
                    equipmentItemRepository.findAllSuitableForServiceInRoom(service.getId(), availableRoom.get().getId());
            availableEqItem = getFirstAvailableEqItem(suitableEqItems, currentCheckStart, currentCheckEnd);
            if (availableEqItem.isEmpty()) {
                return Optional.empty();
            }
        }

        Term term = new Term(currentCheckStart, currentCheckEnd);
        term.setServiceId(service.getId());
        term.setWorkerId(worker.getId());
        term.setRoomId(availableRoom.get().getId());
        if (!itemless) {
            term.setEqItemId(availableEqItem.get().getId());
        }
        return Optional.of(term);
    }

    private Date getIntervalEnd(Calendar currentlyCheckedTime, int serviceDurationInMinutes) {
        Calendar temp = (Calendar) currentlyCheckedTime.clone();
        temp.add(Calendar.MINUTE, serviceDurationInMinutes);
        return temp.getTime();
    }

    // TODO: refactor - code repetition
    //

    private Optional<Room> getFirstAvailableRoom(List<Room> suitableRooms, Date currentCheckStart, Date currentCheckEnd) {
        for (Room room : suitableRooms) {
            if (isRoomFree(currentCheckStart, currentCheckEnd, room)) {
                return Optional.of(room);
            }
        }
        return Optional.empty();
    }

    private Optional<EquipmentItem> getFirstAvailableEqItem(List<EquipmentItem> suitableEqItems, Date currentCheckStart, Date currentCheckEnd) {
        for (EquipmentItem item : suitableEqItems) {
            if (isEqItemFree(currentCheckStart, currentCheckEnd, item)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    private boolean isServiceItemless(Long serviceId) {
        List<EquipmentModel> models = this.equipmentModelRepository.findByService(serviceId);
        return models.isEmpty();
    }

    public boolean isWorkerFree(Date start, Date end, User worker) {
        List<Schedule> existingWorkerEvents =
                (List<Schedule>) workerScheduleRepository.findAnyBetween(start, end, worker.getId());
        return existingWorkerEvents.isEmpty();
    }

    private boolean isRoomFree(Date start, Date end, Room room) {
        List<Schedule> existingRoomEvents =
                (List<Schedule>) roomScheduleRepository.findAnyBetween(start, end, room.getId());
        return existingRoomEvents.isEmpty();
    }

    public boolean isEqItemFree(Date start, Date end, EquipmentItem suitableItem) {
        List<Schedule> existingItemEvents =
                (List<Schedule>) equipmentScheduleRepository.findAnyBetween(start, end, suitableItem.getId());
        return existingItemEvents.isEmpty();
    }
}
