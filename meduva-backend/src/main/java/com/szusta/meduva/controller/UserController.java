package com.szusta.meduva.controller;

import com.szusta.meduva.model.ERole;
import com.szusta.meduva.model.User;
import com.szusta.meduva.payload.request.UpdatedUserRequest;
import com.szusta.meduva.service.RoleService;
import com.szusta.meduva.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping("/all")
    public List<User> findAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/all/undeleted")
    public List<User> findAllUndeletedUsers() {
        return userService.findAllUndeleted();
    }

    @GetMapping("/workers")
    public List<User> getWorkers() {
        return userService.findAllUsersWithMinimumRole(ERole.ROLE_WORKER);
    }

    @GetMapping("/clients")
    public List<User> getClientsWithAccount() {
        return userService.findAllClientsWithAccount();
    }

    @PostMapping("/edit/{id}")
    public User editUser(@PathVariable Long id, @Valid @RequestBody UpdatedUserRequest request){

        User user = userService.getUser(id);

        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setPhoneNumber(request.getPhoneNumber());

        return userService.save(user);
    }
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        this.userService.markAsDeleted(id);
    }
}
