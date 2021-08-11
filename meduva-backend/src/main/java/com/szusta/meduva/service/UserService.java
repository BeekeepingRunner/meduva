package com.szusta.meduva.service;

import com.szusta.meduva.model.Role;
import com.szusta.meduva.model.User;

public interface UserService {

    User saveUser(User user);
    Role saveRole(Role role);

    void addRoleToUser(String login, String roleName);

    User getUser(String login);
}
