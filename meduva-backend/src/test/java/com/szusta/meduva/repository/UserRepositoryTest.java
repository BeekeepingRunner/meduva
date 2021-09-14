package com.szusta.meduva.repository;

import com.szusta.meduva.model.Role;
import com.szusta.meduva.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    @Autowired
    private UserRepository userRepositoryUnderTest;

    @Test
    void findAllClientsWithAccount() {
        // given
        User user = new User(
                1L,
                "login",
                "email",
                "password",
                "name",
                "surname",
                "phoneNumber",
                Set.of(new Role(1L, "ROLE_CLIENT"))
        );
        userRepositoryUnderTest.save(user);

        // when
        Optional<List<User>> clients =
                userRepositoryUnderTest.findAllClientsWithAccount();

        // then
        assertEquals(user, clients.get().get(0));
    }
}