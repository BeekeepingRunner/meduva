package com.szusta.meduva.service;

import com.szusta.meduva.model.Role;
import com.szusta.meduva.model.User;
import com.szusta.meduva.repository.RoleRepository;
import com.szusta.meduva.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

// TODO:
// Exception handling, validation (?)

@Service
@Transactional
@Slf4j // for logs
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User getUserById(Long id) {
        if (userRepository.findById(id).isPresent()) {
            log.info("Getting user {}", userRepository.findById(id).get());
            return userRepository.findById(id).get();
        } else {
            throw new RuntimeException("user not found");
        }
    }

    public User getUserByLogin(String login) {
        log.info("Fetching user {}", login);
        return userRepository.findByLogin(login);
    }

    public User saveUser(User user) {
        log.info("Saving new user {} to the database", user.getLogin());
        return userRepository.save(user);
    }

    public Role saveRole(Role role) {
        log.info("Saving new role {} to the database", role.getName());
        return roleRepository.save(role);
    }

    public void addRoleToUser(String login, String roleName) {
        log.info("Adding role {} to user {}", roleName, login);

        User user = userRepository.findByLogin(login);
        Role role = roleRepository.findByName(roleName);

        user.getRoles().add(role);
    }
}
