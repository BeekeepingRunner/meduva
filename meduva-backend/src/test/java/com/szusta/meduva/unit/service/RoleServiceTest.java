package com.szusta.meduva.unit.service;

import com.szusta.meduva.exception.EntityRecordNotFoundException;
import com.szusta.meduva.model.Role;
import com.szusta.meduva.repository.RoleRepository;
import com.szusta.meduva.service.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    RoleRepository roleRepository;

    @Autowired
    @InjectMocks
    RoleService roleService;

    @Test
    public void findByNameTest() {

        String goodName = "name";
        String badName = "eman";

        when(roleRepository.findByName(goodName))
                .thenReturn(Optional.of(new Role(1L, "ROLE_CLIENT")));
        when(roleRepository.findByName(badName))
                .thenReturn(Optional.empty());

        roleService.findByName(goodName);
        assertThrows(EntityRecordNotFoundException.class, () -> roleService.findByName(badName));

        verify(roleRepository, times(1)).findByName(goodName);
        verify(roleRepository, times(1)).findByName(badName);
    }
}
