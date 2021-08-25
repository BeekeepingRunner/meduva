package com.szusta.meduva.controller;

import com.szusta.meduva.model.User;
import com.szusta.meduva.service.UserAccountService;
import com.szusta.meduva.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PasswordResetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserAccountService userAccountService;

    @Mock
    private UserService userService;

    @InjectMocks
    private PasswordResetController passwordResetController;

    @BeforeAll
    private void setup() {
        userService.save(new User(1, "samplemail@gmail.com", "12345678", ))
    }

    @Test
    public void shouldReturnOk() throws Exception {

        String sampleMail = "samplemail@gmail.com";
        doNothing().when(userAccountService).sendResetPasswordEmail(sampleMail);

        this.mockMvc.perform(
                post("/api/password/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sampleMail))
                .andExpect(status().isOk());

        verify(userAccountService, times(1)).sendResetPasswordEmail(sampleMail);
    }
}