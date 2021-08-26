package com.szusta.meduva.service;

import com.szusta.meduva.exception.PasswordResetTokenNotFoundException;
import com.szusta.meduva.model.PasswordResetToken;
import com.szusta.meduva.model.User;
import com.szusta.meduva.repository.PasswordResetTokenRepository;
import com.szusta.meduva.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserAccountServiceTest {

    @MockBean
    UserService userService;
    @MockBean
    JwtUtils jwtUtils;
    @MockBean
    PasswordResetTokenRepository passwordResetTokenRepository;
    @MockBean
    JavaMailSender javaMailSender;

    @Autowired
    @InjectMocks
    UserAccountService userAccountService;

    private static User user;

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
    }

    @Test
    @DisplayName("Should use given dependencies")
    public void sendResetPasswordEmailTest() {

        when(userService.findByEmail(user.getEmail())).thenReturn(user);
        when(jwtUtils.generateJwtToken(UserDetailsImpl.build(user))).thenReturn("tokenString");
        when(jwtUtils.getJwtExpirationMs()).thenReturn(1000);
        when(passwordResetTokenRepository.deleteByUser(user)).thenReturn(0);

        userAccountService.sendResetPasswordEmail(user.getEmail());

        verify(userService, times(1)).findByEmail(user.getEmail());
        verify(jwtUtils, times(1)).generateJwtToken(UserDetailsImpl.build(user));
        verify(jwtUtils, times(1)).getJwtExpirationMs();
        verify(passwordResetTokenRepository, times(1)).deleteByUser(user);
    }

    @Test
    public void getUserFromResetTokenTest() {
        when(passwordResetTokenRepository.findByToken("token")).thenReturn(
                Optional.of(new PasswordResetToken(user, "token", new Date())));

        assertEquals(user, userAccountService.getUserFromResetToken("token"));
        assertThrows(PasswordResetTokenNotFoundException.class, () -> {
            userAccountService.getUserFromResetToken("badToken");
        });
    }
}
