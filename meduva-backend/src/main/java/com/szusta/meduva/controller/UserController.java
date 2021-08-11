package com.szusta.meduva.controller;

import com.szusta.meduva.model.Role;
import com.szusta.meduva.model.User;
import com.szusta.meduva.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

// TODO:
// Exception handling, validation

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("user/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.getUserById(id));
    }

    @GetMapping("user/search/{login}")
    public ResponseEntity<User> getUserByLogin(@PathVariable String login) {
        return ResponseEntity.ok().body(userService.getUserByLogin(login));
    }

    @PostMapping("/user")
    public ResponseEntity<User> saveUser(@RequestBody User user) {

        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath().path("/api/user")
                        .toUriString());

        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @PostMapping("/role")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {

        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath().path("/api/role")
                        .toUriString());

        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @PostMapping("/role/add-to-user")
    public ResponseEntity<Role> addRoleToUser(@RequestBody RoleToUserForm form) {

        userService.addRoleToUser(form.getUserLogin(), form.getRoleName());
        return ResponseEntity.ok().build();
    }
}

@Data
class RoleToUserForm {
    private String userLogin;
    private String roleName;
}