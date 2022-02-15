package com.szusta.meduva.repository;

import com.szusta.meduva.model.User;
import com.szusta.meduva.model.role.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void given_UserObject_whenSave_thenReturnSavedUser() {
        // given
        User user = new User("login", "email", "password", "name", "surname", "123123123");

        // when
        User savedUser = userRepository.save(user);

        // then
        Assertions.assertThat(savedUser).isNotNull();
    }

}