package com.szusta.meduva.service;

import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.UnregisteredClient;
import com.szusta.meduva.model.User;
import com.szusta.meduva.repository.RoleRepository;
import com.szusta.meduva.repository.ServiceRepository;
import com.szusta.meduva.repository.UnregisteredClientRepository;
import com.szusta.meduva.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

@org.springframework.stereotype.Service
public class WorkerService {

    UserRepository userRepository;
    UnregisteredClientRepository unregisteredClientRepository;
    RoleRepository roleRepository;
    ServiceRepository serviceRepository;

    @Autowired
    public WorkerService(UserRepository userRepository,
                         UnregisteredClientRepository unregisteredClientRepository,
                         RoleRepository roleRepository,
                         ServiceRepository serviceRepository) {
        this.userRepository = userRepository;
        this.unregisteredClientRepository = unregisteredClientRepository;
        this.roleRepository = roleRepository;
        this.serviceRepository = serviceRepository;
    }

    public List<User> findAllByService(Service service) {
        return userRepository.findAllByPerformedService(service.getId());
    }

    public Service[] getWorkerServices(User worker) {
        return worker.getServices().toArray(new Service[0]);
    }

    public List<User> findWorkerClients(Long workerId) {
        return userRepository.findAllClientsOfWorker(workerId);
    }

    public List<UnregisteredClient> findWorkerUnregisteredClients(Long workerId) {
        return unregisteredClientRepository.findAllUnregisteredClientsOfWorker(workerId);
    }
}
