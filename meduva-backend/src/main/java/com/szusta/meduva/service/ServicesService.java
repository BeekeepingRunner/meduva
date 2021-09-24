package com.szusta.meduva.service;

import com.szusta.meduva.exception.AlreadyExistsException;
import com.szusta.meduva.exception.EntityRecordNotFoundException;
import com.szusta.meduva.model.Service;
import com.szusta.meduva.repository.EquipmentItemRepository;
import com.szusta.meduva.repository.ServiceRepository;
import com.szusta.meduva.service.entityconnections.ServiceToEqModelService;
import com.szusta.meduva.util.UndeletableWithNameUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@org.springframework.stereotype.Service
public class ServicesService {

    private ServiceRepository serviceRepository;
    private ServiceToEqModelService serviceToEqModelService;
    private EquipmentItemRepository equipmentItemRepository;

    @Autowired
    public ServicesService(ServiceRepository serviceRepository,
                           ServiceToEqModelService serviceToEqModelService,
                           EquipmentService equipmentService) {
        this.serviceRepository = serviceRepository;
        this.serviceToEqModelService = serviceToEqModelService;
        this.equipmentItemRepository = equipmentItemRepository;
    }

    public List<Service> findAllServices() {
        return this.serviceRepository.findAll();
    }

    public List<Service> findAllUndeletedServices() {
        return this.serviceRepository.findAllUndeleted();
    }

    public Service findById(Long serviceId) {
        return this.serviceRepository.findById(serviceId)
                .orElseThrow(() -> new EntityRecordNotFoundException("Service not found with id : " + serviceId));
    }

    public List<Service> findWithIds(List<Long> servicesIds) {
        return serviceRepository.findAllById(servicesIds);
    }

    public Service save(Service service) {

        if (UndeletableWithNameUtils.canBeSaved(this.serviceRepository, service.getName())) {
            return this.serviceRepository.save(service);
        } else
            throw new AlreadyExistsException("Service already exists with name: " + service.getName());
    }

    public void deleteById(Long id) {
        this.serviceRepository.deleteById(id);
    }

    @Transactional
    public void markAsDeleted(Long id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Service not found with id : " + id));

        serviceToEqModelService.deactivateModelsWithLastService(service);

        service.setEquipmentModel(Collections.emptyList());

        service.markAsDeleted();
        serviceRepository.save(service);
    }
}
