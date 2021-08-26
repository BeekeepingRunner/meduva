package com.szusta.meduva.controller;

import com.szusta.meduva.service.UserAccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PasswordResetControllerTest {

    @MockBean
    private UserAccountService userAccountService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /request success")
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