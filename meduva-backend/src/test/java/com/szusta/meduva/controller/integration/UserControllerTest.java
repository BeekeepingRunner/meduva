package com.szusta.meduva.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.szusta.meduva.model.User;
import com.szusta.meduva.service.user.UserService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @BeforeEach
    void setup() {
        user = new User(
                "login",
                "email@mail.com",
                "password",
                "name",
                "surname",
                "123123123");
    }

    @Nested
    class GetUserTests {

        @Test
        @WithMockUser(
                authorities = {"ROLE_CLIENT", "ROLE_WORKER", "ROLE_RECEPTIONIST"},
                username = "receptionist", password = "1234"
        )
        public void should_returnUser_when_correctId() throws Exception {
            // given
            User expectedUser = user;
            long id = 1L;
            expectedUser.setId(id);
            when(userService.findById(id)).thenReturn(expectedUser);

            mockMvc.perform(get("/api/user/find/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.login", CoreMatchers.is(expectedUser.getLogin())))
                    .andExpect(jsonPath("$.email", CoreMatchers.is(expectedUser.getEmail())));
        }

        @Test
        @WithMockUser(
                authorities = {"ROLE_CLIENT"},
                username = "client", password = "1234"
        )
        public void should_blockAccess_when_unauthorized() throws Exception {
            // given
            Long id = 1L;

            // when
            mockMvc.perform(get("/api/user/find/{id}", id))
                    // then
                    .andExpect(status().isForbidden());
        }
    }

}