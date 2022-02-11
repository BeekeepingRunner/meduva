package com.szusta.meduva.configuration;

import com.szusta.meduva.model.User;
import com.szusta.meduva.model.role.Role;
import com.szusta.meduva.service.user.UserDetailsImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;
import java.util.Set;

@TestConfiguration
public class SpringSecurityWebAuthTestConfig {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {

        User clientUser = new User("clientuser", "client@company.com", "password1");
        clientUser.setId(1L);
        clientUser.setRoles(Set.of(new Role(1L, "ROLE_CLIENT")));

        User workerUser = new User("workeruser", "worker@company.com", "password2");
        workerUser.setId(2L);
        workerUser.setRoles(Set.of(new Role(1L, "ROLE_CLIENT"), new Role(2L, "ROLE_WORKER")));

        User receptionistUser = new User("receptionistuser", "receptionist@company.com", "password3");
        receptionistUser.setId(3L);
        receptionistUser.setRoles(Set.of(
                new Role(1L, "ROLE_CLIENT"),
                new Role(2L, "ROLE_WORKER"),
                new Role(3L, "ROLE_RECEPTIONIST")));

        User adminUser = new User("adminuser", "admin@company.com", "password4");
        adminUser.setId(4L);
        receptionistUser.setRoles(Set.of(
                new Role(1L, "ROLE_CLIENT"),
                new Role(2L, "ROLE_WORKER"),
                new Role(3L, "ROLE_RECEPTIONIST"),
                new Role(4L, "ROLE_ADMIN")));

        UserDetails activeClientUser = UserDetailsImpl.build(clientUser);
        UserDetails activeWorkerUser = UserDetailsImpl.build(workerUser);
        UserDetails activeReceptionistUser = UserDetailsImpl.build(receptionistUser);
        UserDetails activeAdminUser = UserDetailsImpl.build(adminUser);

        return new InMemoryUserDetailsManager(Arrays.asList(
                activeClientUser,
                activeWorkerUser,
                activeReceptionistUser,
                activeAdminUser
        ));
    }
}
