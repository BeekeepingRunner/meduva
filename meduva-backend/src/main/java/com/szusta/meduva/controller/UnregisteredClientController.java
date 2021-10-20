package com.szusta.meduva.controller;

import com.szusta.meduva.model.UnregisteredClient;
import com.szusta.meduva.service.UnregisteredClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/unregistered-client")
public class UnregisteredClientController {

    UnregisteredClientService clientService;

    @Autowired
    public UnregisteredClientController(UnregisteredClientService clientService) {
        this.clientService = clientService;
    }

    /*
    @GetMapping("/find/{id}")
    public UnregisteredClient getById(@PathVariable Long id) {
        return clientService.getUnregisteredClient(id);
    }
     */

    @GetMapping("/all")
    public List<UnregisteredClient> findAll() {
        return clientService.findAll();
    }

    @GetMapping("/all/undeleted")
    public List<UnregisteredClient> findAllUndeleted() {
        return clientService.findAllUndeleted();
    }

    /*
    @PostMapping("/edit/{id}")
    public UnregisteredClient edit(@PathVariable Long id, @Valid @RequestBody UpdatedUserRequest request){

        User user = userService.getUser(id);

        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setPhoneNumber(request.getPhoneNumber());

        return userService.save(user);
    }
     */
}
