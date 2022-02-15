package com.szusta.meduva.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.szusta.meduva.model.User;
import com.szusta.meduva.service.user.UserService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Nested
    class GetUserTests {

        @Test
        @WithMockUser(
                authorities = {"ROLE_CLIENT", "ROLE_WORKER", "ROLE_RECEPTIONIST"},
                username = "receptionist", password = "1234"
        )
        public void should_returnUser_when_correctId() throws Exception {
            // given
            User expectedUser = new User();
            Long id = 1L;
            expectedUser.setId(id);
            when(userService.findById(id)).thenReturn(expectedUser);

            // when
            MvcResult mvcResult = mockMvc.perform(get("/api/user/find/{id}", id))
                    .andReturn();

            String userJSON = mvcResult.getResponse().getContentAsString();
            User actual = objectMapper.readValue(userJSON, User.class);

            // then
            assertEquals(expectedUser.getId(), actual.getId());
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