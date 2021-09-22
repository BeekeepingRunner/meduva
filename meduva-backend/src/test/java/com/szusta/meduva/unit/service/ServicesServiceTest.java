package com.szusta.meduva.unit.service;

import com.szusta.meduva.exception.ServiceAlreadyExistsException;
import com.szusta.meduva.model.Service;
import com.szusta.meduva.repository.ServiceRepository;
import com.szusta.meduva.service.ServicesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicesServiceTest {

    @Mock
    private ServiceRepository serviceRepository;
    @InjectMocks
    private ServicesService servicesServiceUnderTest;

    @Test
    void getAllServices() {
        when(serviceRepository.findAll()).thenReturn(new ArrayList<>());
        servicesServiceUnderTest.findAllServices();
        verify(serviceRepository, times(1)).findAll();
    }

    @Test
    void getAllUnDeletedServices() {
        Service s1 = new Service();
        s1.setDeleted(true);
        Service s2 = new Service();
        s1.setDeleted(false);

        List<Service> services = new ArrayList<>();
        services.add(s1);
        services.add(s2);

        when(serviceRepository.findAll()).thenReturn(services);
        List<Service> undeletedServices = servicesServiceUnderTest.findAllUnDeletedServices();
        undeletedServices.forEach(service -> {
            assertFalse(service.isDeleted());
        });
        verify(serviceRepository, times(1)).findAll();
    }

    @Test
    void saveSuccessWhenServiceDoesntExists() {
        Service service = new Service();
        service.setName("service");

        when(serviceRepository.existsByName(service.getName()))
                .thenReturn(false);
        when(serviceRepository.save(service)).thenReturn(service);

        assertEquals(service, servicesServiceUnderTest.save(service));
        ArgumentCaptor<Service> serviceArgumentCaptor = ArgumentCaptor.forClass(Service.class);

        verify(serviceRepository, times(1)).existsByName(service.getName());
        verify(serviceRepository, times(1)).save(serviceArgumentCaptor.capture());

        Service capturedService = serviceArgumentCaptor.getValue();
        assertEquals(service, capturedService);
    }

    @Test
    void saveSuccessWhenServiceDoesExistsAndIsMarkedAsDeleted() {
        Service service = new Service();
        service.setName("service");
        service.setDeleted(true);

        when(serviceRepository.existsByName(service.getName()))
                .thenReturn(true);
        when(serviceRepository.findByName(service.getName()))
                .thenReturn(Optional.of(service));

        when(serviceRepository.save(service)).thenReturn(service);

        assertEquals(service, servicesServiceUnderTest.save(service));
        ArgumentCaptor<Service> serviceArgumentCaptor = ArgumentCaptor.forClass(Service.class);

        verify(serviceRepository, times(1)).existsByName(service.getName());
        verify(serviceRepository, times(1)).save(serviceArgumentCaptor.capture());

        Service capturedService = serviceArgumentCaptor.getValue();
        assertEquals(service, capturedService);
    }

    @Test
    void saveFail() {
        Service service = new Service();
        service.setName("service");
        service.setDeleted(false);

        when(serviceRepository.existsByName(service.getName()))
                .thenReturn(true);
        when(serviceRepository.findByName(service.getName()))
                .thenReturn(Optional.of(service));

        assertThrows(ServiceAlreadyExistsException.class,
                () -> servicesServiceUnderTest.save(service));

        verify(serviceRepository, times(1)).existsByName(service.getName());
        verify(serviceRepository, never()).save(service);
    }

    @Test
    void markAsDeleted() {
        Service service = new Service();
        service.setId(1L);
        when(serviceRepository.findById(service.getId())).thenReturn(Optional.of(service));
        when(serviceRepository.save(service)).thenReturn(service);

        servicesServiceUnderTest.markAsDeleted(service.getId());

        verify(serviceRepository, times(1)).findById(service.getId());
        verify(serviceRepository, times(1)).save(service);
    }
}