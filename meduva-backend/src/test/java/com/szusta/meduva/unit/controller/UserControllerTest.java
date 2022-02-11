package com.szusta.meduva.unit.controller;

import com.szusta.meduva.model.User;
import com.szusta.meduva.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @LocalServerPort
    private int port;

    private URL base;

    @MockBean
    private UserService userService;

    @Autowired
    private TestRestTemplate template;

    @BeforeEach
    public void setup() throws MalformedURLException {
        this.base = new URL("http://localhost:" + port + "/api/user");
    }

    @Test
    @WithMockUser(
            authorities = {"ROLE_USER", "ROLE_WORKER", "ROLE_RECEPTIONIST"},
            username = "receptionist", password = "1234"
    )
    public void getUser() {
        // given
        User expectedUser = new User();
        Long id = 1L;
        when(userService.findById(id)).thenReturn(expectedUser);

        // when
        ResponseEntity<User> response
                = template.getForEntity(base.toString() + "/find/{id}", User.class, id);
        User actual = response.getBody();

        // then
        assertEquals(expectedUser, actual);
    }
}