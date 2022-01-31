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
    UserRepository userRepositoryMock;
    @Mock
    RoleRepository roleRepositoryMock;

    @InjectMocks
    UserService userService;

    @Nested
    class findByEmailTests {

        @Test
        void should_ReturnUserFromRepository() {
            // given
            String email = "email";
            when(userRepositoryMock.findByEmail(anyString()))
                    .thenReturn(Optional.of(new User()));

            // when
            User user = userService.findByEmail(email);

            assertAll(
                    () -> assertNotNull(user),
                    () -> verify(userRepositoryMock).findByEmail(email)
            );
        }

        @Test
        void should_ThrowException_When_UserNotFound() {
            // given
            String email = "email";
            when(userRepositoryMock.findByEmail(anyString())).thenThrow(EntityRecordNotFoundException.class);

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
            when(userRepositoryMock.findById(anyLong()))
                    .thenReturn(Optional.of(new User()));

            // when
            User user = userService.findById(id);

            assertAll(
                    () -> assertNotNull(user),
                    () -> verify(userRepositoryMock).findById(id)
            );
        }

        @Test
        void should_ThrowException_When_UserNotFound() {
            // given
            long id = 1L;
            when(userRepositoryMock.findById(anyLong())).thenThrow(EntityRecordNotFoundException.class);

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
            when(userRepositoryMock.existsByEmail(anyString())).thenReturn(true);

            // when
            boolean result = userService.existsByEmail(email);

            // then
            assertAll(
                    () -> verify(userRepositoryMock).existsByEmail(email),
                    () -> assertTrue(result)
            );
        }
    }

    @Nested
    class SaveTests {
        @Test
        void should_saveUserInRepository_when_correctUser() {
            when(userRepositoryMock.save(any()))
                    .thenReturn(new User());

            User user = userService.save(new User());

            assertAll(
                    () -> assertNotNull(user),
                    () -> verify(userRepositoryMock).save(any())
            );
        }

        @Test
        void should_throwException_when_nullUser() {
            User user = null;
            when(userRepositoryMock.save(user))
                    .thenThrow(IllegalArgumentException.class);

            Executable executable = () -> userService.save(user);

            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, executable),
                    () -> verify(userRepositoryMock).save(user)
            );
        }
    }

    @Nested
    class FindAllTests {
        @Test
        void should_returnUserListUsingRepo() {
            when(userRepositoryMock.findAll())
                    .thenReturn(List.of(new User(), new User()));

            List<User> users = userService.findAll();

            assertAll(
                    () -> assertEquals(2, users.size()),
                    () -> verify(userRepositoryMock).findAll()
            );
        }

        @Test
        void should_returnEmptyList_when_noUserExist() {
            // given that userRepositoryMock returns empty list by default

            // when
            List<User> users = userService.findAll();

            // then
            assertAll(
                    () -> assertEquals(0, users.size()),
                    () -> verify(userRepositoryMock).findAll()
            );
        }
    }

    @Nested
    class FindAllUndeletedTests {
        @Test
        void should_returnUserListUsingRepo() {
            when(userRepositoryMock.findAllUndeleted())
                    .thenReturn(List.of(new User(), new User()));

            List<User> users = userService.findAllUndeleted();

            assertAll(
                    () -> assertEquals(2, users.size()),
                    () -> verify(userRepositoryMock).findAllUndeleted()
            );
        }

        @Test
        void should_returnEmptyList_when_noUserExist() {
            // given that userRepositoryMock returns empty list by default

            // when
            List<User> users = userService.findAllUndeleted();

            // then
            assertAll(
                    () -> assertEquals(0, users.size()),
                    () -> verify(userRepositoryMock).findAllUndeleted()
            );
        }
    }

    @Nested
    class FindAllUsersWithMinimumRoleTests {

        @Test
        public void should_returnUsersUsingDependencies_when_correctRole() {
            // given
            ERole roleId = ERole.ROLE_CLIENT;
            List<User> users = List.of(new User(), new User());
            Role role = new Role(1L, "ROLE_CLIENT");
            when(roleRepositoryMock.findById(roleId.getValue()))
                    .thenReturn(Optional.of(role));
            when(userRepositoryMock.findDistinctByRolesIn(Collections.singleton(role)))
                    .thenReturn(users);

            // when
            List<User> got = userService.findAllUsersWithMinimumRole(roleId);

            // then
            assertAll(
                    () -> assertEquals(2, got.size()),
                    () -> verify(roleRepositoryMock).findById(roleId.getValue()),
                    () -> verify(userRepositoryMock).findDistinctByRolesIn(Collections.singleton(role))
            );
        }

        @Test
        public void should_ThrowException_when_roleDoesntExist() {
            // given
            ERole roleId = ERole.ROLE_RECEPTIONIST;
            when(roleRepositoryMock.findById(roleId.getValue()))
                    .thenReturn(Optional.empty());

            // when
            Executable executable = () -> userService.findAllUsersWithMinimumRole(roleId);

            // then
            assertAll(
                    () -> assertThrows(EntityRecordNotFoundException.class, executable),
                    () -> verify(roleRepositoryMock).findById(roleId.getValue())
            );
        }

        @Test
        public void should_ThrowException_when_noUsersWithSpecifiedRole() {
            // given
            ERole roleId = ERole.ROLE_RECEPTIONIST;
            Role receptionist = new Role(3L, "ROLE_RECEPTIONIST");
            when(roleRepositoryMock.findById(roleId.getValue()))
                    .thenReturn(Optional.of(receptionist));
            when(userRepositoryMock.findDistinctByRolesIn(Collections.singleton(receptionist)))
                    .thenReturn(new ArrayList<>());

            // when
            Executable executable = () -> userService.findAllUsersWithMinimumRole(roleId);

            // then
            assertAll(
                    () -> assertThrows(EntityRecordNotFoundException.class, executable),
                    () -> verify(roleRepositoryMock).findById(roleId.getValue()),
                    () -> verify(userRepositoryMock).findDistinctByRolesIn(Collections.singleton(receptionist))
            );
        }
    }


    @Test
    @DisplayName("test findAllClientsWithAccount() success")
    public void testFindAllClientsWithAccount() {

        User client = new User();
        when(userRepositoryMock.findAllClientsWithAccount())
                .thenReturn(Optional.of(List.of(client)));

        userService.findAllClientsWithAccount();

        verify(userRepositoryMock, times(1)).findAllClientsWithAccount();
    }

    @Test
    @DisplayName("test findAllClientsWithAccount() exception")
    public void shouldThrowRuntimeException() {

        when(userRepositoryMock.findAllClientsWithAccount())
                .thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> userService.findAllClientsWithAccount());

        verify(userRepositoryMock, times(1)).findAllClientsWithAccount();
    }
}
