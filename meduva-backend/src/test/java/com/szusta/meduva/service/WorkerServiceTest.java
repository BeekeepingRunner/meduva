package com.szusta.meduva.service;

import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.User;
import com.szusta.meduva.repository.RoleRepository;
import com.szusta.meduva.repository.ServiceRepository;
import com.szusta.meduva.repository.UnregisteredClientRepository;
import com.szusta.meduva.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WorkerServiceTest {

    @Mock UserRepository userRepository;
    @Mock UnregisteredClientRepository unregisteredClientRepository;
    @Mock RoleRepository roleRepository;
    @Mock ServiceRepository serviceRepository;

    @InjectMocks
    WorkerService workerService;

    @Test
    void should_ReturnPlainArrayOfServicesOfWorker() {

        // given
        User worker = new User();
        Set<Service> services = new HashSet<>(Set.of(
           new Service("service1", "desc1", 15, new BigDecimal("100.0"), true, false),
           new Service("service2", "desc2", 15, new BigDecimal("100.0"), true, false),
           new Service("service3", "desc3", 15, new BigDecimal("100.0"), true, false),
           new Service("service4", "desc4", 15, new BigDecimal("100.0"), true, false)
        ));
        worker.setServices(services);

        // when
        Service[] actual = workerService.getWorkerServices(worker);

        // then
        assertEquals(4, actual.length);
    }
}