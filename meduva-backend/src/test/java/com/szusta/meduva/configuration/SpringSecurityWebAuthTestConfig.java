package com.szusta.meduva.configuration;

import com.szusta.meduva.model.User;
import com.szusta.meduva.service.user.UserDetailsImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Arrays;

@TestConfiguration
public class SpringSecurityWebAuthTestConfig {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {

        User basicUser = new User("Basic User", "user@company.com", "password");
        basicUser.setId(1L);
        UserDetails basicActiveUser = UserDetailsImpl.build(basicUser);
        UserDetails basicActiveUser = new UserDetailsImpl(basicUser, Arrays.asList(
                new SimpleGrantedAuthority("ROLE_CLIENT")
        ));

        User managerUser = new UserImpl("Manager User", "manager@company.com", "password");
        UserActive managerActiveUser = new UserActive(managerUser, Arrays.asList(
                new SimpleGrantedAuthority("ROLE_MANAGER"),
                new SimpleGrantedAuthority("PERM_FOO_READ"),
                new SimpleGrantedAuthority("PERM_FOO_WRITE"),
                new SimpleGrantedAuthority("PERM_FOO_MANAGE")
        ));

        return new InMemoryUserDetailsManager(Arrays.asList(
                basicActiveUser, managerActiveUser
        ));
    }
}
