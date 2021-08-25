package com.szusta.meduva.controller;

import com.szusta.meduva.model.User;
import com.szusta.meduva.service.RoleService;
import com.szusta.meduva.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    UserService userService;
    RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/find/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }
}
