package com.szusta.meduva.service;

import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.User;
import com.szusta.meduva.repository.RoleRepository;
import com.szusta.meduva.repository.ServiceRepository;
import com.szusta.meduva.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class WorkerService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    ServiceRepository serviceRepository;

    @Autowired
    public WorkerService(UserRepository userRepository,
                         RoleRepository roleRepository,
                         ServiceRepository serviceRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.serviceRepository = serviceRepository;
    }

    public List<User> findAllByService(Service service) {
        return userRepository.findAllByPerformedService(service.getId());
    }
}
