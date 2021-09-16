package com.szusta.meduva.service;

import com.szusta.meduva.exception.ServiceAlreadyExistsException;
import com.szusta.meduva.exception.notfound.ServiceNotFoundException;
import com.szusta.meduva.model.Service;
import com.szusta.meduva.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;

@org.springframework.stereotype.Service
public class ServicesService {

    private ServiceRepository serviceRepository;

    @Autowired
    public ServicesService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public List<Service> findAllServices() {
        return this.serviceRepository.findAll();
    }

    public List<Service> findAllUnDeletedServices() {
        return this.serviceRepository.findAllUndeleted();
    }

    public Service save(Service service) {

        String serviceName = service.getName();
        if (this.serviceRepository.existsByName(serviceName) && isNotDeleted(serviceName)) {
            throw new ServiceAlreadyExistsException("Service already exists with name: " + service.getName());
        } else
            return this.serviceRepository.save(service);
    }

    private boolean isNotDeleted(String serviceName) {
        Service service = this.serviceRepository.findByName(serviceName)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found with name: " + serviceName));

        return !service.isDeleted();
    }

    public void deleteById(Long id) {
        this.serviceRepository.deleteById(id);
    }

    @Transactional
    public void markAsDeleted(Long id) {
        Service service = this.serviceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found with id : " + id));

        service.markAsDeleted();
        this.serviceRepository.save(service);
    }
}
