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

        when(userRepository.existsByLogin("login")).thenReturn(true);
        when(userRepository.existsByLogin("loginn")).thenReturn(false);
        assertEquals(true, userService.existsByLogin("login"));
        assertEquals(false, userService.existsByLogin("loginn"));
        verify(userRepository, times(1)).existsByLogin("login");
        verify(userRepository, times(1)).existsByLogin("loginn");
    }

    @Test
    @DisplayName("existsByEmail test")
    public void existsByEmailTest() {

        when(userRepository.existsByEmail("email")).thenReturn(true);
        when(userRepository.existsByEmail("liame")).thenReturn(false);

        assertEquals(true, userService.existsByEmail("email"));
        assertEquals(false, userService.existsByEmail("liame"));

        verify(userRepository, times(1)).existsByEmail("email");
        verify(userRepository, times(1)).existsByEmail("liame");
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
