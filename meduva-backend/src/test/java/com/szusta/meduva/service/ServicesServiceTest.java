package com.szusta.meduva.service;

import com.szusta.meduva.exception.ServiceAlreadyExistsException;
import com.szusta.meduva.model.Service;
import com.szusta.meduva.repository.ServiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

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
        servicesServiceUnderTest.getAllServices();
        verify(serviceRepository, times(1)).findAll();
    }

    @Test
    void saveSuccess() {
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
    void saveFail() {
        Service service = new Service();
        service.setName("service");

        when(serviceRepository.existsByName(service.getName()))
                .thenReturn(true);

        assertThrows(ServiceAlreadyExistsException.class,
                () -> servicesServiceUnderTest.save(service));

        verify(serviceRepository, times(1)).existsByName(service.getName());
    }
}