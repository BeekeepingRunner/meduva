package com.szusta.meduva.service;

import com.szusta.meduva.exception.RoleNotFoundException;
import com.szusta.meduva.model.ERole;
import com.szusta.meduva.model.Role;
import com.szusta.meduva.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findByName(ERole name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RoleNotFoundException("Error: Role not found:" + name.toString()));
    }
}
