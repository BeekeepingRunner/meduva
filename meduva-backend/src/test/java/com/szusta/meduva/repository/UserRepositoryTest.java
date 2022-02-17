package com.szusta.meduva.repository;

import com.szusta.meduva.model.User;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void given_UserObject_whenSave_thenReturnSavedUser() {
        // given
        User user = new User("login", "email@mail.com", "password", "name", "surname", "123123123");

        // when
        User savedUser = userRepository.save(user);

        // then
        assertAll(
                () -> assertThat(savedUser).isNotNull(),
                () -> assertThat(savedUser.getId()).isGreaterThan(0)
        );
    }

    @Nested
    class DeleteTests {

        @Test
        void given_UserObject_whenDelete_thenRemoveUser() {
            // given
            User user = new User("login", "email@mail.com", "password", "name", "surname", "123123123");
            userRepository.save(user);

            // when
            userRepository.delete(user);
            Optional<User> userOptional = userRepository.findById(user.getId());

            // then
            assertTrue(userOptional.isEmpty());
        }

        @Test
        void given_UserObject_whenDeleteById_thenRemoveUser() {
            // given
            User user = new User("login", "email@mail.com", "password", "name", "surname", "123123123");
            userRepository.save(user);

            // when
            userRepository.deleteById(user.getId());
            Optional<User> userOptional = userRepository.findById(user.getId());

            // then
            assertTrue(userOptional.isEmpty());
        }
    }

    @Nested
    class FindByLoginTests {

        @Test
        void given_savedUser_whenCorrectLogin_thenReturnSavedUser() {
            // given
            User user = new User("login", "email@mail.com", "password", "name", "surname", "123123123");
            userRepository.save(user);

            // when
            User actual = userRepository.findByLogin(user.getLogin()).get();

            // then
            assertAll(
                    () -> assertThat(actual.getId()).isEqualTo(user.getId()),
                    () -> assertThat(actual.getLogin()).isEqualTo(user.getLogin())
            );
        }

        @Test
        void given_savedUser_whenIncorrectLogin_thenReturnEmptyOptional() {
            // given
            User user = new User("login", "email@mail.com", "password", "name", "surname", "123123123");
            userRepository.save(user);

            // when
            Optional<User> actual = userRepository.findByLogin("wrong");

            // then
            assertTrue(actual.isEmpty());
        }
    }

    @Nested
    class FindByEmailTests {

        @Test
        void given_savedUser_whenCorrectEmail_thenReturnSavedUser() {
            // given
            User user = new User("login", "email@mail.com", "password", "name", "surname", "123123123");
            userRepository.save(user);

            // when
            User actual = userRepository.findByEmail(user.getEmail()).get();

            // then
            assertAll(
                    () -> assertThat(actual.getId()).isEqualTo(user.getId()),
                    () -> assertThat(actual.getEmail()).isEqualTo(user.getEmail())
            );
        }

        @Test
        void given_savedUser_whenIncorrectEmail_thenReturnEmptyOptional() {
            // given
            User user = new User("login", "email@mail.com", "password", "name", "surname", "123123123");
            userRepository.save(user);

            // when
            Optional<User> actual = userRepository.findByEmail("wrong");

            // then
            assertTrue(actual.isEmpty());
        }
    }

    @Nested
    class ExistsByLoginTests {

        @Test
        void given_existingUser_whenCorrectLogin_thenReturnTrue() {
            // given
            User user = new User("login", "email@mail.com", "password", "name", "surname", "123123123");
            userRepository.save(user);

            // when
            boolean doesUserExist = userRepository.existsByLogin(user.getLogin());

            // then
            assertTrue(doesUserExist);
        }

        @Test
        void given_existingUser_whenIncorrectLogin_thenReturnFalse() {
            // given
            User user = new User("login", "email@mail.com", "password", "name", "surname", "123123123");
            userRepository.save(user);

            // when
            boolean doesUserExist = userRepository.existsByLogin("wrong");

            // then
            assertFalse(doesUserExist);
        }
    }

    @Nested
    class ExistsByEmailTests {

        @Test
        void given_existingUser_whenCorrectEmail_thenReturnTrue() {
            // given
            User user = new User("login", "email@mail.com", "password", "name", "surname", "123123123");
            userRepository.save(user);

            // when
            boolean doesUserExist = userRepository.existsByEmail(user.getEmail());

            // then
            assertTrue(doesUserExist);
        }

        @Test
        void given_existingUser_whenIncorrectEmail_thenReturnFalse() {
            // given
            User user = new User("login", "email@mail.com", "password", "name", "surname", "123123123");
            userRepository.save(user);

            // when
            boolean doesUserExist = userRepository.existsByEmail("wrong");

            // then
            assertFalse(doesUserExist);
        }
    }
}