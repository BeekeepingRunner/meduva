package com.szusta.meduva.service;

import com.szusta.meduva.exception.UserNotFoundException;
import com.szusta.meduva.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    UserRepository userRepository;

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

        assertThrows(UserNotFoundException.class, () -> userService.getUser(1L));
        assertThrows(UserNotFoundException.class, () -> userService.findByLogin("login"));
        assertThrows(UserNotFoundException.class, () -> userService.findByEmail("email"));

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findByLogin("login");
        verify(userRepository, times(1)).findByEmail("email");
    }
}
