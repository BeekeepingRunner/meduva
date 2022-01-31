package com.szusta.meduva.unit.service;

import com.szusta.meduva.exception.EntityRecordNotFoundException;
import com.szusta.meduva.model.role.ERole;
import com.szusta.meduva.model.role.Role;
import com.szusta.meduva.model.User;
import com.szusta.meduva.repository.RoleRepository;
import com.szusta.meduva.repository.UserRepository;
import com.szusta.meduva.service.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    UserService userService;

    @Nested
    class findByEmailTests {

        @Test
        void should_ReturnUserFromRepository() {
            // given
            String email = "email";
            when(userRepository.findByEmail(anyString()))
                    .thenReturn(Optional.of(new User()));

            // when
            User user = userService.findByEmail(email);

            assertAll(
                    () -> assertNotNull(user),
                    () -> verify(userRepository).findByEmail(email)
            );
        }

        @Test
        void should_ThrowException_When_UserNotFound() {
            // given
            String email = "email";
            when(userRepository.findByEmail(anyString())).thenThrow(EntityRecordNotFoundException.class);

            // when, then
            assertThrows(EntityRecordNotFoundException.class, () -> userService.findByEmail(email));
        }
    }

    @Nested
    class findByIdTests {

        @Test
        void should_ReturnUserFromRepository() {
            // given
            long id = 1L;
            when(userRepository.findById(anyLong()))
                    .thenReturn(Optional.of(new User()));

            // when
            User user = userService.findById(id);

            assertAll(
                    () -> assertNotNull(user),
                    () -> verify(userRepository).findById(id)
            );
        }

        @Test
        void should_ThrowException_When_UserNotFound() {
            // given
            long id = 1L;
            when(userRepository.findById(anyLong())).thenThrow(EntityRecordNotFoundException.class);

            // when, then
            assertThrows(EntityRecordNotFoundException.class, () -> userService.findById(id));
        }
    }

    @Nested
    class existsByEmailTests {
        @Test
        public void should_returnValueFromRepository() {
            // given
            String email = "email";
            when(userRepository.existsByEmail(anyString())).thenReturn(true);

            // when
            boolean result = userService.existsByEmail(email);

            // then
            assertAll(
                    () -> verify(userRepository).existsByEmail(email),
                    () -> assertTrue(result)
            );
        }
    }

    @Nested
    class SaveTests {
        @Test
        void should_saveUserInRepository_when_correctUser() {
            when(userRepository.save(any()))
                    .thenReturn(new User());

            User user = userService.save(new User());

            assertAll(
                    () -> assertNotNull(user),
                    () -> verify(userRepository).save(any())
            );
        }

        @Test
        void should_throwException_when_incorrectUser() {
            when(userRepository.save(any()))
                    .thenThrow(IllegalArgumentException.class);

            Executable executable = () -> userService.save(new User());

            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, executable),
                    () -> verify(userRepository).save(any())
            );
        }
    }


    @Test
    void should_return_whenFindingAllUsers() {
        when(userRepository.findAll())
                .thenReturn(List.of(new User(), new User()));

        User user = userService.save(new User());

        assertAll(
                () -> assertNotNull(user),
                () -> verify(userRepository).save(any())
        );
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
