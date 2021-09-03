package com.szusta.meduva.service;

import com.szusta.meduva.exception.RoleNotFoundException;
import com.szusta.meduva.model.Role;
import com.szusta.meduva.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RoleServiceTest {

    @MockBean
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
        assertThrows(RoleNotFoundException.class, () -> roleService.findByName(badName));

        verify(roleRepository, times(1)).findByName(goodName);
        verify(roleRepository, times(1)).findByName(badName);
    }
}
