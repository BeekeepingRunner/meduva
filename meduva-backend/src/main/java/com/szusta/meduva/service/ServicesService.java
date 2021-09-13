package com.szusta.meduva.service;

import com.szusta.meduva.model.Service;
import com.szusta.meduva.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class ServicesService {

    private ServiceRepository serviceRepository;

    @Autowired
    public ServicesService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public List<Service> getAllServices() {
        return this.serviceRepository.findAll();
    }

    public Service save(Service service) {
        return this.serviceRepository.save(service);
    }
}
