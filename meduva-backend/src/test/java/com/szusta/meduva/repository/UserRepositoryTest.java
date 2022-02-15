package com.szusta.meduva.repository;

import com.szusta.meduva.model.role.ERole;
import com.szusta.meduva.model.User;
import com.szusta.meduva.repository.RoleRepository;
import com.szusta.meduva.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepositoryUnderTest;
    @Autowired
    private RoleRepository roleRepository;

    @Test
    void shouldFindUserWithClientRoleOnly() {
        // given
        User userClient = new User(
                "login",
                "email",
                "password",
                "name",
                "surname",
                "phoneNumber"
        );
        userClient.setRoles(Set.of(roleRepository.getById(ERole.ROLE_CLIENT.getValue())));
        userClient = userRepositoryUnderTest.save(userClient);

        User userWorker = new User(
                "login2",
                "email2",
                "password2",
                "name2",
                "surname2",
                "phoneNumber2"
        );
        userWorker.setRoles(Set.of(
                roleRepository.getById(ERole.ROLE_CLIENT.getValue()),
                roleRepository.getById(ERole.ROLE_WORKER.getValue())));
        userWorker = userRepositoryUnderTest.save(userWorker);

        // when
        Optional<List<User>> clients =
                userRepositoryUnderTest.findAllClientsWithAccount();

        // then
        assertTrue(clients.isPresent());
        assertEquals(1, clients.get().size());
        assertEquals(userClient.getName(), clients.get().get(0).getName());
    }

    @Test
    void shouldFindUsersWithMinimumRoles() {
        // given
        User userClient = new User(
                "login",
                "email",
                "password",
                "name",
                "surname",
                "phoneNumber"
        );
        userClient.setRoles(Set.of(roleRepository.getById(ERole.ROLE_CLIENT.getValue())));
        userClient = userRepositoryUnderTest.save(userClient);

        User userWorker = new User(
                "login2",
                "email2",
                "password2",
                "name2",
                "surname2",
                "phoneNumber2"
        );
        userWorker.setRoles(Set.of(
                roleRepository.getById(ERole.ROLE_CLIENT.getValue()),
                roleRepository.getById(ERole.ROLE_WORKER.getValue())));
        userWorker = userRepositoryUnderTest.save(userWorker);

        // when
        List<User> users =
                userRepositoryUnderTest.findDistinctByRolesIn(Set.of(
                        roleRepository.getById(ERole.ROLE_CLIENT.getValue()),
                        roleRepository.getById(ERole.ROLE_WORKER.getValue())));

        // then
        assertFalse(users.isEmpty());
        assertEquals(2, users.size());
        assertEquals(userClient.getName(), users.get(0).getName());
    }

}