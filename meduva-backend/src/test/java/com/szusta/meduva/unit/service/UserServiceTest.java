package com.szusta.meduva.unit.service;

import com.szusta.meduva.exception.EntityRecordNotFoundException;
import com.szusta.meduva.model.role.ERole;
import com.szusta.meduva.model.role.Role;
import com.szusta.meduva.model.User;
import com.szusta.meduva.repository.RoleRepository;
import com.szusta.meduva.repository.UserRepository;
import com.szusta.meduva.service.user.UserDetailsImpl;
import com.szusta.meduva.service.user.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

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

    @Nested
    class FindAllClientsWithAccountTests {

        @Test
        public void should_returnUserList_when_registeredClientsInRepo() {

            // given
            when(userRepositoryMock.findAllClientsWithAccount())
                    .thenReturn(Optional.of(List.of(new User(), new User())));

            // when
            List<User> registeredClients = userService.findAllClientsWithAccount();

            // then
            assertAll(
                    () -> assertEquals(2, registeredClients.size()),
                    () -> verify(userRepositoryMock).findAllClientsWithAccount()
            );
        }

        @Test
        public void should_throwException_when_registeredClientsAreNotInRepo() {

            // given
            when(userRepositoryMock.findAllClientsWithAccount())
                    .thenThrow(EntityRecordNotFoundException.class);

            // when
            Executable executable = () -> userService.findAllClientsWithAccount();

            // then
            assertThrows(EntityRecordNotFoundException.class, executable);
        }
    }

    @Nested
    class GetCurrentUserIdTests {

        /*
        These two methods require preceding static mocking of SecurityContextHolder class
         */
        private SecurityContext mockSecurityContext(MockedStatic<SecurityContextHolder> contextHolderMock) {
            SecurityContext context = mock(SecurityContext.class);
            contextHolderMock.when(SecurityContextHolder::getContext)
                    .thenReturn(context);
            return context;
        }
        private Authentication mockAuthentication(SecurityContext securityContext) {
            Authentication auth = mock(Authentication.class);
            when(securityContext.getAuthentication()).thenReturn(auth);
            return auth;
        }

        @Test
        void should_returnCurrentUserId_When_userIsInTheContext() {
            try (MockedStatic<SecurityContextHolder> contextHolderMock = mockStatic(SecurityContextHolder.class)) {
                // given
                SecurityContext context = mockSecurityContext(contextHolderMock);
                Authentication authentication = mockAuthentication(context);

                final long expectedUserId = 1L;
                UserDetails userDetails = new UserDetailsImpl(expectedUserId, "username", "email", "password", Collections.emptyList());
                when(authentication.getPrincipal()).thenReturn(userDetails);

                User user = new User();
                user.setId(expectedUserId);
                when(userRepositoryMock.findByLogin(userDetails.getUsername()))
                        .thenReturn(Optional.of(user));

                // when
                final Long actualUserId = userService.getCurrentUserId();

                // then
                assertEquals(actualUserId, expectedUserId);
            }
        }

        @Test
        void should_ThrowException_when_noSecurityContextAvailable() {
            try (MockedStatic<SecurityContextHolder> contextHolderMock = mockStatic(SecurityContextHolder.class)) {
                // given:
                // returns null authentication by default
                SecurityContext context = mockSecurityContext(contextHolderMock);

                // when
                Executable executable = () -> userService.getCurrentUserId();

                // then
                assertThrows(AuthenticationCredentialsNotFoundException.class, executable);
            }
        }

        @Test
        void should_ThrowException_When_userAuthenticatedButNotFoundInRepo() {
            try (MockedStatic<SecurityContextHolder> contextHolderMock = mockStatic(SecurityContextHolder.class)) {
                // given
                SecurityContext context = mockSecurityContext(contextHolderMock);
                Authentication authentication = mockAuthentication(context);

                UserDetails userDetails = new UserDetailsImpl(1L, "username", "email", "password", Collections.emptyList());
                when(authentication.getPrincipal()).thenReturn(userDetails);

                when(userRepositoryMock.findByLogin(anyString()))
                        .thenReturn(Optional.empty());

                // when
                Executable executable = () -> userService.getCurrentUserId();

                // then
                assertThrows(EntityRecordNotFoundException.class, executable);
            }
        }
    }

    @Nested
    class changeUserRoleTests {

        @Test
        void should_returnUserWithNewRole_When_CorrectUserId() {
            // given
            Long userId = 1L;
            User user = createTestUser(userId);
            Long oldRoleId = 1L;
            user.setRoles(Set.of(new Role(oldRoleId, "ROLE_CLIENT")));

            when(userRepositoryMock.findById(userId))
                    .thenReturn(Optional.of(user));

            Long newRoleId = 2L;
            Role oldRole = new Role(oldRoleId, "ROLE_CLIENT");
            Role newRole = new Role(newRoleId, "ROLE_WORKER");
            when(roleRepositoryMock.findById(oldRoleId))
                    .thenReturn(Optional.of(oldRole));
            when(roleRepositoryMock.findById(newRoleId))
                    .thenReturn(Optional.of(newRole));

            when(userRepositoryMock.save(user))
                    .thenReturn(user);

            // when
            User userWithNewRole = userService.changeUserRole(userId, newRoleId);

            // then
            Set<Role> userRoles = userWithNewRole.getRoles();
            assertTrue(userRoles.contains(newRole));
        }

        @Test
        void should_returnUserWithoutHigherRole_When_NewRoleIsLowerThanOldOne() {
            // given
            Long userId = 1L;
            User user = createTestUser(userId);

            Long lowerRoleId = 1L;
            Long higherRoleId = 2L;
            Role lowerRole = new Role(lowerRoleId, "ROLE_CLIENT");
            Role higherRole = new Role(higherRoleId, "ROLE_WORKER");

            user.setRoles(Set.of(
                    new Role(lowerRoleId, "ROLE_CLIENT"),
                    new Role(higherRoleId, "ROLE_WORKER")));

            when(userRepositoryMock.findById(userId))
                    .thenReturn(Optional.of(user));
            when(roleRepositoryMock.findById(lowerRoleId))
                    .thenReturn(Optional.of(lowerRole));
            when(userRepositoryMock.save(user))
                    .thenReturn(user);

            // when
            User userWithNewRole = userService.changeUserRole(userId, lowerRoleId);

            // then
            Set<Role> userRoles = userWithNewRole.getRoles();
            assertAll(
                    () -> assertTrue(userRoles.contains(lowerRole)),
                    () -> assertFalse(userRoles.contains(higherRole))
            );
        }

        @Test
        void should_throwException_When_UserWithIdDoesntExist() {
            // given
            Long userId = 1L;
            User user = createTestUser(userId);
            when(userRepositoryMock.findById(anyLong()))
                    .thenThrow(EntityRecordNotFoundException.class);

            // when
            Executable executable = () -> userService.changeUserRole(userId, 1L);

            // then
            assertAll(
                    () -> assertThrows(EntityRecordNotFoundException.class, executable),
                    () -> verify(roleRepositoryMock, never()).findById(anyLong()),
                    () -> verify(userRepositoryMock, never()).save(any())
            );
        }

        @Test
        void should_throwException_When_IncorrectRoleId() {
            // given
            Long userId = 1L;
            User user = createTestUser(userId);
            when(userRepositoryMock.findById(userId))
                    .thenReturn(Optional.of(user));
            when(roleRepositoryMock.findById(anyLong()))
                    .thenThrow(EntityRecordNotFoundException.class);

            // when
            Executable executable = () -> userService.changeUserRole(userId, 1L);

            // then
            assertAll(
                    () -> assertThrows(EntityRecordNotFoundException.class, executable),
                    () -> verify(userRepositoryMock).findById(userId),
                    () -> verify(userRepositoryMock, never()).save(any())
            );
        }

        private User createTestUser(Long id) {
            User user = new User("login", "email", "password", "name", "surname", "123123123");
            user.setId(id);
            return user;
        }
    }
}
