package com.szusta.meduva.controller;

import com.szusta.meduva.model.Service;
import com.szusta.meduva.payload.request.NewServiceRequest;
import com.szusta.meduva.service.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service")
public class ServicesController {

    private ServicesService servicesService;

    @Autowired
    public ServicesController(ServicesService servicesService) {
        this.servicesService = servicesService;
    }

    @GetMapping("/all")
    public List<Service> findAllServices() {
        return this.servicesService.findAllServices();
    }

    @GetMapping("/all/undeleted")
    public List<Service> findAllUndeletedServices() {
        return this.servicesService.findAllUndeletedServices();
    }

    @GetMapping("/all/itemless")
    public List<Service> findAllItemlessServices(){ return this.servicesService.findAllItemless(); }

    @PostMapping
    public Service saveNewService(@RequestBody NewServiceRequest request) {
        Service service = new Service(
                request.getName(),
                request.getDescription(),
                request.getDurationInMin(),
                request.getPrice(),
                request.isItemless(),
                request.isDeleted()
        );
        return this.servicesService.save(service);
    }

    @DeleteMapping("/{id}")
    public void deleteService(@PathVariable Long id) {
        this.servicesService.markAsDeleted(id);
    }
}
