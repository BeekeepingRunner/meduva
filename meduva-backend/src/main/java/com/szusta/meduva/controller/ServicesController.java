package com.szusta.meduva.controller;

import com.szusta.meduva.model.Service;
import com.szusta.meduva.service.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/service")
public class ServicesController {

    private ServicesService servicesService;

    @Autowired
    public ServicesController(ServicesService servicesService) {
        this.servicesService = servicesService;
    }

    @RequestMapping("/all")
    public List<Service> getAllServices() {
        return this.servicesService.getAllServices();
    }
}
