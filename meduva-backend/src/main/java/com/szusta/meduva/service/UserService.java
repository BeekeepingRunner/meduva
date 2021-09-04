package com.szusta.meduva.service;

import com.szusta.meduva.exception.UserNotFoundException;
import com.szusta.meduva.exception.WorkersNotFoundException;
import com.szusta.meduva.model.ERole;
import com.szusta.meduva.model.Role;
import com.szusta.meduva.model.User;
import com.szusta.meduva.repository.RoleRepository;
import com.szusta.meduva.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User findByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("User not found with login : " + login));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email : " + email));
    }

    public Boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findAllWorkers() {

        List<Long> workerRolesId = List.of(
                ERole.ROLE_WORKER.getValue());

        List<Role> workerRoles = roleRepository.findAllById(workerRolesId);

        return userRepository.findByRolesIn(workerRoles)
                .orElseThrow(() -> new WorkersNotFoundException("Unable to find any worker"));
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("user not found with id : " + id));
    }
}
