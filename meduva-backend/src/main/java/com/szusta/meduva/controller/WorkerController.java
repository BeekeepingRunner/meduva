package com.szusta.meduva.controller;

import com.szusta.meduva.model.ERole;
import com.szusta.meduva.model.Service;
import com.szusta.meduva.model.User;
import com.szusta.meduva.payload.request.ChangeRoleRequest;
import com.szusta.meduva.payload.request.UpdatedUserRequest;
import com.szusta.meduva.service.RoleService;
import com.szusta.meduva.service.UserService;
import com.szusta.meduva.service.entityconnections.UserToServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
public class WorkerController {

    UserToServiceService userToServiceService;

    @Autowired
    public WorkerController(UserToServiceService userToServiceService) {
        this.userToServiceService = userToServiceService;
    }

    @GetMapping("/workerServices/{id}")
    public Service[] getWorkerServices(@PathVariable Long id) {
        return userToServiceService.getWorkerServices(id);
    }

    @PostMapping("/assignServicesToWorker/{id}")
    public User assignServicesToWorker(@PathVariable Long id, @RequestBody Long[] request){
        return userToServiceService.assignServicesToWorker(id, request);

    }
}