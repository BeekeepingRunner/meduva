package com.szusta.meduva.unit.service;

import com.szusta.meduva.model.role.Role;
import com.szusta.meduva.model.User;
import com.szusta.meduva.repository.UserRepository;
import com.szusta.meduva.service.user.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDetailServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Autowired
    @InjectMocks
    UserDetailsServiceImpl userDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(
                "login",
                "email@email.com",
                "password",
                "name",
                "surname",
                "48123123123"
        );
        user.setRoles(Set.of(
                new Role(1L, "ROLE_CLIENT"),
                new Role(2L, "ROLE_WORKER")));
    }

    @Test
    @DisplayName("loadUserByUserName should return valid UserDetails")
    public void loadUserByUserNameValidUserDetailsTest() {

        String username = "login";
        when(userRepository.findByLogin(username)).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        assertEquals(user.getLogin(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());

        Collection<GrantedAuthority> grantedAuthorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        assertEquals(grantedAuthorities, userDetails.getAuthorities());

        verify(userRepository, times(1)).findByLogin(username);
    }

    @Test
    @DisplayName("loadUserByUserName should throw UsernameNotFoundException")
    public void loadUserByUserNameShouldThrowException() {

        String badUsername = "login";
        when(userRepository.findByLogin(badUsername)).thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(badUsername));

        verify(userRepository, times(1)).findByLogin(badUsername);
    }
}
