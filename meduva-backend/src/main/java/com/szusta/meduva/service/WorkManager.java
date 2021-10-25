package com.szusta.meduva.service;

import com.szusta.meduva.exception.EntityRecordNotFoundException;
import com.szusta.meduva.model.User;
import com.szusta.meduva.model.WorkHours;
import com.szusta.meduva.payload.WorkHoursPayload;
import com.szusta.meduva.repository.ServiceRepository;
import com.szusta.meduva.repository.UserRepository;
import com.szusta.meduva.repository.WorkHoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class WorkManager {

    UserRepository userRepository;
    ServiceRepository serviceRepository;
    WorkHoursRepository workHoursRepository;

    ScheduleChecker scheduleChecker;

    @Autowired
    public WorkManager(UserRepository userRepository,
                       ServiceRepository serviceRepository,
                       WorkHoursRepository workHoursRepository,
                       ScheduleChecker scheduleChecker) {
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
        this.workHoursRepository = workHoursRepository;
        this.scheduleChecker = scheduleChecker;
    }

    public com.szusta.meduva.model.Service[] getWorkerServices(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityRecordNotFoundException("User not found with id : " + userId));

        Set<com.szusta.meduva.model.Service> serviceSet = user.getServices();

        com.szusta.meduva.model.Service[] serviceIdTable = new com.szusta.meduva.model.Service[serviceSet.size()];

        int ItemInTableCounter=0;
        for (com.szusta.meduva.model.Service s : serviceSet) {
            serviceIdTable[ItemInTableCounter++]=s;
        }
        return serviceIdTable;
    }

    @Transactional
    public User assignServicesToWorker(Long userId, Long[] servicesId){
        User user = userRepository.findById(userId).orElseThrow(()-> new EntityRecordNotFoundException("User not found with id : " + userId));

        Set<com.szusta.meduva.model.Service> serviceSet= new HashSet<>();

        for(Long s : servicesId){
            com.szusta.meduva.model.Service serv = serviceRepository.findById(s).orElseThrow(()-> new EntityRecordNotFoundException("Service not found with id : " + s));
            serviceSet.add(serv);
        }

        user.setServices(serviceSet);

        return userRepository.save(user);
    }


    public WorkHours setWorkHours(User worker, WorkHoursPayload workHoursPayload) {

        // TODO: make sure that there aren't any worker's visits
        //  before and after requested work hours

        Date newWorkStartTime = workHoursPayload.getStartTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(workHoursPayload.getStartTime());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        boolean hasNotAnyVisits =
            scheduleChecker.isWorkerFree(calendar.getTime(), newWorkStartTime, worker);

        Date newWorkEndTime = workHoursPayload.getEndTime();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        hasNotAnyVisits = scheduleChecker.isWorkerFree(newWorkEndTime, calendar.getTime(), worker);

        if (hasNotAnyVisits) {
            WorkHours workHours = new WorkHours(
                    workHoursPayload.getStartTime(), workHoursPayload.getEndTime());
            workHours.setWorker(worker);
            return workHoursRepository.save(workHours);
        } else {
            return null;
        }
    }
}
