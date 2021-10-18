package com.szusta.meduva.service;

import com.szusta.meduva.exception.AlreadyExistsException;
import com.szusta.meduva.exception.BadRequestRoleException;
import com.szusta.meduva.model.User;
import com.szusta.meduva.model.role.Role;
import com.szusta.meduva.payload.request.SignupRequest;
import com.szusta.meduva.service.user.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    public AuthService(UserService userService,
                       RoleService roleService,
                       PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    public void checkForExistingCredentials(String login, String email) {

        if (userService.existsByLogin(login)) {
            throw new AlreadyExistsException("That login is already taken : " + login);
        }
        if (userService.existsByEmail(email)) {
            throw new AlreadyExistsException("Email is already in use : " + email);
        }
    }

    public void saveNewUserFrom(SignupRequest request) {

        Set<Role> roles = extractRequestRoles(request.getRoles());

        User user = new User(
                request.getLogin(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getName(),
                request.getSurname(),
                request.getPhoneNumber());

        user.setRoles(roles);
        userService.save(user);
    }

    private Set<Role> extractRequestRoles(Set<String> requestRoles) {

        Set<Role> userRoles = new HashSet<>();

        if (requestRoles == null) {
            addDefaultRoleTo(userRoles);
        } else {
            addRoles(requestRoles, userRoles);
        }

        return userRoles;
    }

    private void addDefaultRoleTo(Set<Role> userRoles) {
        userRoles.add(roleService.findByName("ROLE_CLIENT"));
    }

    private void addRoles(Set<String> requestRoles, Set<Role> userRoles) {

        requestRoles.forEach(role -> {
            switch (role) {
                case "ROLE_ADMIN":
                    userRoles.add(roleService.findByName("ROLE_ADMIN"));
                    break;
                case "ROLE_RECEPTIONIST":
                    userRoles.add(roleService.findByName("ROLE_RECEPTIONIST"));
                    break;
                case "ROLE_WORKER":
                    userRoles.add(roleService.findByName("ROLE_WORKER"));
                    break;
                case "ROLE_CLIENT":
                    userRoles.add(roleService.findByName("ROLE_CLIENT"));
                    break;
                default:
                    throw new BadRequestRoleException("Bad user role in request body");
            }
        });
    }
}
