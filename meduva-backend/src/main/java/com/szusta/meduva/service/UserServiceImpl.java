package com.szusta.meduva.service;

import com.szusta.meduva.model.Role;
import com.szusta.meduva.model.User;
import com.szusta.meduva.repository.RoleRepository;
import com.szusta.meduva.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;

// TODO:
// Exception handling, validation (?)

@Service
@Transactional
@Slf4j // for logs
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        User user = userRepository.findByLogin(userName);
        if (user == null) {
            log.error("User not found in the database");
            throw new RuntimeException("User not found in the database");
        } else {
            log.error("User not found in the database");
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });

        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassword(),
                authorities);
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
