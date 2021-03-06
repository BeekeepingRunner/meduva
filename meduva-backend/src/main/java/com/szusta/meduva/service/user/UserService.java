package com.szusta.meduva.service.user;

import com.szusta.meduva.exception.EntityRecordNotFoundException;
import com.szusta.meduva.model.User;
import com.szusta.meduva.model.role.ERole;
import com.szusta.meduva.model.role.Role;
import com.szusta.meduva.repository.RoleRepository;
import com.szusta.meduva.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.*;

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
                .orElseThrow(() -> new EntityRecordNotFoundException("User not found with login : " + login));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityRecordNotFoundException("User not found with email : " + email));
    }

    public User findById(long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityRecordNotFoundException("User not found with id: " + id));
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
                    .orElseThrow(() -> new EntityRecordNotFoundException("Unable to find users with role" + role.get().getName()));
        } else {
            throw new EntityRecordNotFoundException("Role not found with id : " + roleId);
        }
    }

    public List<User> findAllClientsWithAccount() {
        return userRepository.findAllClientsWithAccount()
                .orElseThrow(() -> new RuntimeException("Unable to fetch clients with accounts"));
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityRecordNotFoundException("user not found with id : " + id));
    }

    public Long getCurrentUserId() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByLogin(userDetails.getUsername())
                .orElseThrow(() -> {
                    String errorMsg = "Authenticated user couldn't be found in the database (login : " + userDetails.getUsername() + ")";
                    return new EntityRecordNotFoundException(errorMsg);
                });
        return user.getId();
    }

    @Transactional
    public void markAsDeleted(Long userId) {
        if(userId==getCurrentUserId()){
            throw new IllegalArgumentException("Current user cannot delete himself");
        }
        com.szusta.meduva.model.User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id : " + userId));

        user.markAsDeleted();
        userRepository.save(user);
    }

    @Transactional
    public User changeUserRole(Long userId, Long roleId){
        User user = findById(userId);

        Set<Role> roleSet = new HashSet<>();
        for(Long id = 1L; id <= roleId; id++){
            Role role = roleRepository.findById(id)
                    .orElseThrow(() -> new EntityRecordNotFoundException(
                            "Role not found with id : " + roleId));
            roleSet.add(role);
        }

        user.setRoles(roleSet);
        return userRepository.save(user);
    }
}
