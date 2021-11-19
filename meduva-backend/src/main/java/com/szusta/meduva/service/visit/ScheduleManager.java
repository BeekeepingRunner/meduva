package com.szusta.meduva.service.visit;

import com.szusta.meduva.exception.EntityRecordNotFoundException;
import com.szusta.meduva.model.Room;
import com.szusta.meduva.model.User;
import com.szusta.meduva.model.equipment.EquipmentItem;
import com.szusta.meduva.model.role.ERole;
import com.szusta.meduva.model.role.Role;
import com.szusta.meduva.model.schedule.EquipmentSchedule;
import com.szusta.meduva.model.schedule.RoomSchedule;
import com.szusta.meduva.model.schedule.WorkerSchedule;
import com.szusta.meduva.model.schedule.status.EquipmentStatus;
import com.szusta.meduva.model.schedule.status.RoomStatus;
import com.szusta.meduva.model.schedule.status.WorkerStatus;
import com.szusta.meduva.model.schedule.status.enums.EEquipmentStatus;
import com.szusta.meduva.model.schedule.status.enums.ERoomStatus;
import com.szusta.meduva.model.schedule.status.enums.EWorkerStatus;
import com.szusta.meduva.model.schedule.visit.UserVisit;
import com.szusta.meduva.model.schedule.visit.Visit;
import com.szusta.meduva.repository.RoleRepository;
import com.szusta.meduva.repository.schedule.equipment.EquipmentScheduleRepository;
import com.szusta.meduva.repository.schedule.equipment.EquipmentStatusRepository;
import com.szusta.meduva.repository.schedule.room.RoomScheduleRepository;
import com.szusta.meduva.repository.schedule.room.RoomStatusRepository;
import com.szusta.meduva.repository.schedule.worker.WorkerScheduleRepository;
import com.szusta.meduva.repository.schedule.worker.WorkerStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class ScheduleManager {

    private RoomStatusRepository roomStatusRepository;
    private WorkerStatusRepository workerStatusRepository;
    private EquipmentStatusRepository equipmentStatusRepository;

    private RoomScheduleRepository roomScheduleRepository;
    private WorkerScheduleRepository workerScheduleRepository;
    private EquipmentScheduleRepository equipmentScheduleRepository;

    private RoleRepository roleRepository;

    @Autowired
    public ScheduleManager(RoomStatusRepository roomStatusRepository,
                           WorkerStatusRepository workerStatusRepository,
                           EquipmentStatusRepository equipmentStatusRepository,
                           RoomScheduleRepository roomScheduleRepository,
                           WorkerScheduleRepository workerScheduleRepository,
                           EquipmentScheduleRepository equipmentScheduleRepository,
                           RoleRepository roleRepository) {
        this.roomStatusRepository = roomStatusRepository;
        this.workerStatusRepository = workerStatusRepository;
        this.equipmentStatusRepository = equipmentStatusRepository;
        this.roomScheduleRepository = roomScheduleRepository;
        this.workerScheduleRepository = workerScheduleRepository;
        this.equipmentScheduleRepository = equipmentScheduleRepository;
        this.roleRepository = roleRepository;
    }

    public void generateSchedules(Visit visit) {

        generateRoomSchedule(visit);
        generateWorkerSchedule(visit);

        if (!visit.getService().isItemless()) {
            generateEquipmentSchedule(visit);
        }
    }

    private void generateRoomSchedule(Visit visit) {
        RoomSchedule roomSchedule = new RoomSchedule(visit.getRoom(), visit.getTimeFrom(), visit.getTimeTo());
        RoomStatus roomOccupied = roomStatusRepository.getById(ERoomStatus.ROOM_OCCUPIED.getValue());
        roomSchedule.setRoomStatus(roomOccupied);
        roomScheduleRepository.save(roomSchedule);
    }

    private void generateWorkerSchedule(Visit visit) {
        List<User> workers = getWorkers(visit);
        WorkerStatus occupied = workerStatusRepository.getById(EWorkerStatus.WORKER_OCCUPIED.getValue());
        for (User worker : workers) {
            WorkerSchedule workerSchedule = new WorkerSchedule(worker, visit.getTimeFrom(), visit.getTimeTo());
            workerSchedule.setWorkerStatus(occupied);
            workerScheduleRepository.save(workerSchedule);
        }
    }

    private void generateEquipmentSchedule(Visit visit) {
        EquipmentSchedule equipmentSchedule = new EquipmentSchedule(visit.getEqItems().get(0), visit.getTimeFrom(), visit.getTimeTo());
        EquipmentStatus equipmentOccupied = equipmentStatusRepository.getById(EEquipmentStatus.EQUIPMENT_OCCUPIED.getValue());
        equipmentSchedule.setEquipmentStatus(equipmentOccupied);
        equipmentScheduleRepository.save(equipmentSchedule);
    }

    public void freeSchedules(Visit visit) {
        freeWorkerSchedules(visit);
        freeRoomSchedule(visit);
        freeEquipmentSchedules(visit);
    }

    private void freeWorkerSchedules(Visit visit) {
        List<User> workers = getWorkers(visit);
        for (User user : workers) {
            workerScheduleRepository.deleteByWorkerIdBetween(user.getId(), visit.getTimeFrom(), visit.getTimeTo());
        }
    }

    private List<User> getWorkers(Visit visit) {
        List<User> workers = new ArrayList<>();

        List<UserVisit> userVisits = visit.getUserVisits();
        for (UserVisit userVisit : userVisits) {
            User user = userVisit.getUser();
            Set<Role> userRoles = user.getRoles();
            Role workerRole = roleRepository.findById(ERole.ROLE_WORKER.getValue())
                    .orElseThrow(() -> new EntityRecordNotFoundException("Role not found in DB with id : " + ERole.ROLE_WORKER.getValue()));
            if (userRoles.contains(workerRole)) {
                workers.add(user);
            }
        }
        return workers;
    }

    private void freeRoomSchedule(Visit visit) {
        Room room = visit.getRoom();
        roomScheduleRepository.deleteByRoomIdBetween(room.getId(), visit.getTimeFrom(), visit.getTimeTo());
    }

    private void freeEquipmentSchedules(Visit visit) {
        List<EquipmentItem> equipmentItems = visit.getEqItems();
        for (EquipmentItem item : equipmentItems) {
            System.out.println(item.getId());
            equipmentScheduleRepository.deleteByEqItemIdWithStartTime(item.getId(), visit.getTimeFrom());
        }
    }
}
