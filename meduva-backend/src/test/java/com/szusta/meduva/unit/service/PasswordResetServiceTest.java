package com.szusta.meduva.unit.service;

import com.szusta.meduva.exception.notfound.PasswordResetTokenNotFoundException;
import com.szusta.meduva.model.PasswordResetToken;
import com.szusta.meduva.model.User;
import com.szusta.meduva.repository.PasswordResetTokenRepository;
import com.szusta.meduva.security.jwt.JwtUtils;
import com.szusta.meduva.service.PasswordResetService;
import com.szusta.meduva.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PasswordResetServiceTest {

    @Mock
    UserService userService;
    @Mock
    JwtUtils jwtUtils;
    @Mock
    PasswordResetTokenRepository passwordResetTokenRepository;
    @Mock
    JavaMailSender javaMailSender;
    @Mock
    PasswordEncoder passwordEncoder;

    @Autowired
    @InjectMocks
    PasswordResetService passwordResetService;

    private static User user;
    private static String token;
    private static PasswordResetToken resetToken;

    @BeforeAll
    public static void setup() {
        user = new User(
        "login",
        "email@email.com",
        "password",
        "name",
        "surname",
        "48123123123"
        );
        token = "tokenString";
        resetToken = new PasswordResetToken();
    }

    @Test
    @DisplayName("Should delete previous tokens")
    public void deletePreviousResetTokenTest() {
        when(passwordResetTokenRepository.deleteByUser(user)).thenReturn(1);
        passwordResetService.deletePreviousResetTokens(user);
        verify(passwordResetTokenRepository, times(1)).deleteByUser(user);
    }

    @Test
    public void getUserFromResetTokenTest() {
        when(passwordResetTokenRepository.findByToken("token")).thenReturn(
                Optional.of(new PasswordResetToken(user, "token", new Date())));

        assertEquals(user, passwordResetService.getUserFromResetToken("token"));
        assertThrows(PasswordResetTokenNotFoundException.class, () -> {
            passwordResetService.getUserFromResetToken("badToken");
        });
    }
}
