package com.szusta.meduva.controller;

import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.User;
import com.szusta.meduva.service.ServicesService;
import org.aspectj.lang.annotation.AfterThrowing;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServicesControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @Sql("/sql/test.sql")
    void getAllServicesTest() throws Exception {

    }

    @Test
    void saveNewService() {

        Service service = new Service();
        service.setName("service");
        service.setDescription("desc");
        service.setDurationInMin(30);
        service.setPrice(BigDecimal.valueOf(100));

        HttpEntity<Service> request = new HttpEntity<>(service);
        // TODO: handle unauthorized error
        ResponseEntity<Service> response = testRestTemplate.postForEntity("/api/service", request, Service.class);
        assertNotNull(response.getBody().getId());
    }

    @AfterEach
    @AfterThrowing
    @Sql("/sql/testTearDown.sql")
    void tearDown() {

    }
}