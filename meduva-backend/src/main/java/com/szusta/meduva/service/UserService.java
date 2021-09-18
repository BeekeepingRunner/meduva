package com.szusta.meduva.service;

import com.szusta.meduva.exception.notfound.RoleNotFoundException;
import com.szusta.meduva.exception.notfound.UserNotFoundException;
import com.szusta.meduva.exception.UsersWithMinRoleNotFound;
import com.szusta.meduva.model.ERole;
import com.szusta.meduva.model.Role;
import com.szusta.meduva.model.User;
import com.szusta.meduva.repository.RoleRepository;
import com.szusta.meduva.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    public List<User> findAllUndeleted() {
        return this.userRepository.findAllUndeleted();
    }

    public List<User> findAllDeleted() {
        return this.userRepository.findAllDeleted();
    }

    public List<User> findAllUsersWithMinimumRole(ERole roleId) {

        Optional<Role> role = roleRepository.findById(roleId.getValue());
        if (role.isPresent()) {
            return userRepository.findDistinctByRolesIn(Collections.singleton(role.get()))
                    .orElseThrow(() -> new UsersWithMinRoleNotFound("Unable to find users with role" + role.get().getName()));
        } else {
            throw new RoleNotFoundException("Role not found with id: " + roleId);
        }
    }

    public List<User> findAllClientsWithAccount() {
        return userRepository.findAllClientsWithAccount()
                .orElseThrow(() -> new RuntimeException("Unable to fetch clients with accounts"));
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("user not found with id : " + id));
    }
}