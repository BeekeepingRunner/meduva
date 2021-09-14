package com.szusta.meduva.repository;

import com.szusta.meduva.model.ERole;
import com.szusta.meduva.model.Role;
import com.szusta.meduva.model.User;
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
    void findAllClientsWithAccount() {
        // given
        User user = new User(
                "login",
                "email",
                "password",
                "name",
                "surname",
                "phoneNumber"
        );
        user.setRoles(Set.of(roleRepository.getById(ERole.ROLE_CLIENT.getValue())));
        userRepositoryUnderTest.save(user);

        // when
        Optional<List<User>> clients =
                userRepositoryUnderTest.findAllClientsWithAccount();

        // then
        assertEquals(user, clients.get().get(0));
    }
}