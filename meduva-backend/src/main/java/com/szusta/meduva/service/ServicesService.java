package com.szusta.meduva.service;

import com.szusta.meduva.exception.AlreadyExistsException;
import com.szusta.meduva.exception.EntityRecordNotFoundException;
import com.szusta.meduva.model.Service;
import com.szusta.meduva.repository.ServiceRepository;
import com.szusta.meduva.service.equipment.ModelDeactivator;
import com.szusta.meduva.util.UndeletableWithNameUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@org.springframework.stereotype.Service
public class ServicesService {

    private ServiceRepository serviceRepository;
    private ModelDeactivator modelDeactivator;

    @Autowired
    public ServicesService(ServiceRepository serviceRepository,
                           ModelDeactivator modelDeactivator) {
        this.serviceRepository = serviceRepository;
        this.modelDeactivator = modelDeactivator;
    }

    public List<Service> findAllServices() {
        return this.serviceRepository.findAll();
    }

    public List<Service> findAllUndeletedServices() {
        return this.serviceRepository.findAllUndeleted();
    }

    public List<Service> findAllItemless() {
        return this.serviceRepository.findByItemlessTrue();
    }

    public Service findById(Long serviceId) {
        return this.serviceRepository.findById(serviceId)
                .orElseThrow(() -> new EntityRecordNotFoundException("Service not found with id : " + serviceId));
    }

    public boolean doesServiceExistByName(String serviceName) {
        return this.serviceRepository.existsByName(serviceName);
    }

    public Service save(Service service) {

        if (UndeletableWithNameUtils.canBeSaved(this.serviceRepository, service.getName())) {
            return this.serviceRepository.save(service);
        } else
            throw new AlreadyExistsException("Service already exists with name: " + service.getName());
    }

    @Transactional
    public void markAsDeleted(Long serviceId) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("Service not found with id : " + serviceId));

        modelDeactivator.deactivateModelsWithLastService(service);
        service.setEquipmentModel(Collections.emptyList()); // won't that connection be useful in the future though?
        service.markAsDeleted();
        serviceRepository.save(service);
    }
}
