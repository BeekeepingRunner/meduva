package com.szusta.meduva.unit.service;

import com.szusta.meduva.exception.EntityRecordNotFoundException;
import com.szusta.meduva.model.role.ERole;
import com.szusta.meduva.model.role.Role;
import com.szusta.meduva.model.User;
import com.szusta.meduva.repository.RoleRepository;
import com.szusta.meduva.repository.UserRepository;
import com.szusta.meduva.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;

    @Autowired
    @InjectMocks
    UserService userService;

    @Test
    @DisplayName("existsByLogin test")
    public void existsByLoginTest() {

        String goodLogin = "login";
        String badLogin = "loginn";

        when(userRepository.existsByLogin(goodLogin)).thenReturn(true);
        when(userRepository.existsByLogin(badLogin)).thenReturn(false);

        assertEquals(true, userService.existsByLogin(goodLogin));
        assertEquals(false, userService.existsByLogin(badLogin));

        verify(userRepository, times(1)).existsByLogin(goodLogin);
        verify(userRepository, times(1)).existsByLogin(badLogin);
    }

    @Test
    @DisplayName("existsByEmail test")
    public void existsByEmailTest() {

        String goodEmail = "email";
        String badEmail = "liame";

        when(userRepository.existsByEmail(goodEmail)).thenReturn(true);
        when(userRepository.existsByEmail(badEmail)).thenReturn(false);

        assertEquals(true, userService.existsByEmail(goodEmail));
        assertEquals(false, userService.existsByEmail(badEmail));

        verify(userRepository, times(1)).existsByEmail(goodEmail);
        verify(userRepository, times(1)).existsByEmail(badEmail);
    }

    @Test
    public void shouldThrowNotFoundException() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(userRepository.findByLogin("login")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("email")).thenReturn(Optional.empty());

        assertThrows(EntityRecordNotFoundException.class, () -> userService.getUser(1L));
        assertThrows(EntityRecordNotFoundException.class, () -> userService.findByLogin("login"));
        assertThrows(EntityRecordNotFoundException.class, () -> userService.findByEmail("email"));

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findByLogin("login");
        verify(userRepository, times(1)).findByEmail("email");
    }

    @Test
    public void findAllUsersWithMinimumRole() {
        //given
        User worker = new User();
        List<User> users = new ArrayList<>();
        users.add(worker);

        Role clientRole = new Role(ERole.ROLE_CLIENT.getValue(), "ROLE_CLIENT");
        Role workerRole = new Role(ERole.ROLE_WORKER.getValue(), "ROLE_WORKER");

        when(roleRepository.findById(ERole.ROLE_WORKER.getValue()))
                .thenReturn(Optional.of(workerRole));
        when(userRepository.findDistinctByRolesIn(Collections.singleton(workerRole)))
                .thenReturn(Optional.of(users));

        when(roleRepository.findById(ERole.ROLE_CLIENT.getValue()))
                .thenReturn(Optional.of(clientRole));
        when(userRepository.findDistinctByRolesIn(Collections.singleton(clientRole)))
                .thenThrow(EntityRecordNotFoundException.class);

        // good scenario
        List<User> outUsers = userService.findAllUsersWithMinimumRole(ERole.ROLE_WORKER);
        assertEquals(1, outUsers.size());

        // bad scenario
        assertThrows(EntityRecordNotFoundException.class,
                () -> userService.findAllUsersWithMinimumRole(ERole.ROLE_CLIENT));

        verify(roleRepository, times(1))
                .findById(ERole.ROLE_WORKER.getValue());
        verify(roleRepository, times(1))
                .findById(ERole.ROLE_CLIENT.getValue());
        verify(userRepository, times(1))
                .findDistinctByRolesIn(Collections.singleton(clientRole));
        verify(userRepository, times(1))
                .findDistinctByRolesIn(Collections.singleton(workerRole));
    }

    @Test
    @DisplayName("test findAllClientsWithAccount() success")
    public void testFindAllClientsWithAccount() {

        User client = new User();
        when(userRepository.findAllClientsWithAccount())
                .thenReturn(Optional.of(List.of(client)));

        userService.findAllClientsWithAccount();

        verify(userRepository, times(1)).findAllClientsWithAccount();
    }

    @Test
    @DisplayName("test findAllClientsWithAccount() exception")
    public void shouldThrowRuntimeException() {

        when(userRepository.findAllClientsWithAccount())
                .thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> userService.findAllClientsWithAccount());

        verify(userRepository, times(1)).findAllClientsWithAccount();
    }
}
