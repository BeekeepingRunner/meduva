package com.szusta.meduva.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.szusta.meduva.model.PasswordResetToken;
import com.szusta.meduva.model.User;
import com.szusta.meduva.payload.request.password.ResetPasswordRequest;
import com.szusta.meduva.payload.request.password.ResetTokenRequest;
import com.szusta.meduva.service.reset.PasswordResetService;
import com.szusta.meduva.service.user.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.util.Date;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PasswordResetControllerTest {

    @MockBean
    private PasswordResetService passwordResetService;
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private static User user;
    ObjectMapper objectMapper = new ObjectMapper();

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
    @DisplayName("GET /request success")
    public void requestSuccessTest() throws Exception {

        when(userService.findByEmail(user.getEmail())).thenReturn(user);
        PasswordResetToken resetToken = new PasswordResetToken();
        when(passwordResetService.createPasswordResetToken(user)).thenReturn(resetToken);

        doNothing().when(passwordResetService).deletePreviousResetTokens(user);
        doNothing().when(passwordResetService).sendResetPasswordEmail(user.getEmail(), resetToken);

        this.mockMvc.perform(
                post("/api/password/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(user.getEmail()))
                .andExpect(status().isOk());

        verify(userService, times(1)).findByEmail(user.getEmail());
        verify(passwordResetService, times(1)).createPasswordResetToken(user);
        verify(passwordResetService, times(1)).deletePreviousResetTokens(user);
        verify(passwordResetService, times(1)).sendResetPasswordEmail(user.getEmail(), resetToken);
    }

    @Test
    @DisplayName("POST /user success")
    public void getUserWithResetTokenSuccessTest() throws Exception {

        String resetToken = "resetToken";
        when(passwordResetService.getUserFromResetToken(resetToken))
                .thenReturn(new User());

        this.mockMvc.perform(
                post("/api/password/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(resetToken))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new User())));

        verify(passwordResetService, times(1)).getUserFromResetToken(resetToken);
    }

    @Test
    @DisplayName("POST /validate-reset-token OK test")
    public void validateResetTokenOKTest() throws Exception {

        ResetTokenRequest resetTokenReq = new ResetTokenRequest();
        resetTokenReq.setToken("reset");
        User user = new User();

        when(passwordResetService.getResetToken(resetTokenReq.getToken()))
                .thenReturn(new PasswordResetToken(
                        user,
                        "token",
                        Date.from(new Date().toInstant().plus(Duration.ofDays(2)))));

        this.mockMvc.perform(
                        post("/api/password/validate-reset-token")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(resetTokenReq)))
                .andExpect(status().isOk());

        verify(passwordResetService, times(1)).getResetToken(resetTokenReq.getToken());
    }

    @Test
    @DisplayName("POST /validate-reset-token Unauthorized test")
    public void validateResetTokenUnauthorizedTest() throws Exception {

        ResetTokenRequest resetTokenReq = new ResetTokenRequest();
        resetTokenReq.setToken("reset");
        User user = new User();

        when(passwordResetService.getResetToken(resetTokenReq.getToken()))
                .thenReturn(new PasswordResetToken(
                        user,
                        "token",
                        Date.from(new Date().toInstant().minus(Duration.ofSeconds(1)))));

        this.mockMvc.perform(
                        post("/api/password/validate-reset-token")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(resetTokenReq)))
                .andExpect(status().isUnauthorized());

        verify(passwordResetService, times(1)).getResetToken(resetTokenReq.getToken());
    }

    @Test
    @DisplayName("POST /change test")
    public void handlePasswordChangeRequestTest() throws Exception {

        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setResetToken("resetToken");
        request.setPassword("password");
        request.setRepeatPassword("password");

        doNothing().when(passwordResetService).resetPassword(request.getResetToken(), request.getPassword());

        this.mockMvc.perform(
                        post("/api/password/change")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(passwordResetService, times(1))
                .resetPassword(request.getResetToken(), request.getPassword());
    }
}