package com.szusta.meduva.service;

import com.szusta.meduva.exception.AlreadyExistsException;
import com.szusta.meduva.model.Service;
import com.szusta.meduva.repository.ServiceRepository;
import com.szusta.meduva.util.UndeletableWithNameUtils;
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

        if (UndeletableWithNameUtils.canBeSaved(this.serviceRepository, service.getName())) {
            return this.serviceRepository.save(service);
        } else
            throw new AlreadyExistsException("Room already exists with name: " + service.getName());
    }

    public void deleteById(Long id) {
        this.serviceRepository.deleteById(id);
    }

    @Transactional
    public void markAsDeleted(Long id) {
        UndeletableWithNameUtils.markAsDeleted(this.serviceRepository, id);
    }
}
