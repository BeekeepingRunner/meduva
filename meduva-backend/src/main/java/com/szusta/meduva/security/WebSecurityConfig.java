package com.szusta.meduva.security;

import com.szusta.meduva.filter.AuthTokenFilter;
import com.szusta.meduva.security.jwt.AuthEntryPointJwt;
import com.szusta.meduva.service.user.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;

    // a.k.a. Authentication exception handler
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    public WebSecurityConfig(
            UserDetailsServiceImpl userDetailsService,
            AuthEntryPointJwt unauthorizedHandler) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors().and().csrf().disable();

        setExceptionHandler(http);
        setStatelessSessionCreationPolicy(http);

        authorizeRequests(http);

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    private void setExceptionHandler(HttpSecurity http) throws Exception {
        http.exceptionHandling().authenticationEntryPoint(unauthorizedHandler);
    }

    private void setStatelessSessionCreationPolicy(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    private void authorizeRequests(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/test/all").permitAll()
                .antMatchers("/api/greetings").permitAll()
                .antMatchers("/api/password/request").permitAll()
                .antMatchers("/api/password/user").permitAll()
                .antMatchers("/api/password/validate-reset-token").permitAll()
                .antMatchers("/api/password/change").permitAll()

                .antMatchers("/api/creator/all").hasAuthority("ROLE_ADMIN")

                .antMatchers("/api/email/**").hasAuthority("ROLE_CLIENT")

                .antMatchers("/api/room/all").hasAuthority("ROLE_WORKER")
                .antMatchers("/api/room/all/**").hasAuthority("ROLE_WORKER")
                .antMatchers("/api/room/doesExistWithName/{roomName}").hasAuthority("ROLE_WORKER")
                .antMatchers("/api/room/doesExistWithName/{roomName}").hasAuthority("ROLE_WORKER")
                .antMatchers("/api/room/{id}").hasAuthority("ROLE_WORKER")
                .antMatchers("/api/room/get-weekly-unavailability/{roomId}").hasAuthority("ROLE_WORKER")
                .antMatchers("/api/room/set-day-unavailability/{roomId}").hasAuthority("ROLE_RECEPTIONIST")
                .antMatchers("/api/room/delete-day-unavailability/{roomId}").hasAuthority("ROLE_RECEPTIONIST")

                .antMatchers("/api/equipment/models/all/**").hasAuthority("ROLE_WORKER")
                .antMatchers("/api/equipment/model/{id}").hasAuthority("ROLE_WORKER")
                .antMatchers("/api/equipment/model/new").hasAuthority("ROLE_ADMIN")
                .antMatchers("/api/equipment/model/connect").hasAuthority("ROLE_ADMIN")
                .antMatchers("/api/equipment/model/doesExistWithName/{modelName}").hasAuthority("ROLE_ADMIN")
                .antMatchers("/api/equipment/item/{id}").hasAuthority("ROLE_WORKER")
                .antMatchers("/api/equipment/item/get-weekly-unavailability/{itemId}").hasAuthority("ROLE_WORKER")
                .antMatchers("/api/equipment/item/set-day-unavailability/{itemId}").hasAuthority("ROLE_RECEPTIONIST")
                .antMatchers("/api/equipment/item/delete-day-unavailability/{itemId}").hasAuthority("ROLE_RECEPTIONIST")

                .antMatchers("/api/service/all").hasAuthority("ROLE_CLIENT")
                .antMatchers("/api/service/all/**").hasAuthority("ROLE_CLIENT")
                .antMatchers("/api/service/service/doesExistWithName/{serviceName}").hasAuthority("ROLE_CLIENT")
                .antMatchers("/api/service/{id}").hasAuthority("ROLE_WORKER")

                .antMatchers("/api/unregistered-client/find/{id}").hasAuthority("ROLE_WORKER")
                .antMatchers("/api/unregistered-client/all").hasAuthority("ROLE_WORKER")
                .antMatchers("/api/unregistered-client/all/undeleted").hasAuthority("ROLE_WORKER")
                .antMatchers("/api/unregistered-client/add").hasAuthority("ROLE_WORKER")
                .antMatchers("/api/unregistered-client/edit").hasAuthority("ROLE_RECEPTIONIST")
                .antMatchers("/api/unregistered-client/{id}").hasAuthority("ROLE_ADMIN")

                .antMatchers("/api/user/find/{id}").hasAuthority("ROLE_RECEPTIONIST")
                .antMatchers("/api/user/all").hasAuthority("ROLE_RECEPTIONIST")
                .antMatchers("/api/user/all/undeleted").hasAuthority("ROLE_RECEPTIONIST")
                .antMatchers("/api/user/workers").hasAuthority("ROLE_RECEPTIONIST")
                .antMatchers("/api/user/clients").hasAuthority("ROLE_RECEPTIONIST")
                .antMatchers("/api/user/edit/{id}").hasAuthority("ROLE_ADMIN")
                .antMatchers("/api/user/{id}").hasAuthority("ROLE_ADMIN")
                .antMatchers("/api/user/edit-role/{id}").hasAuthority("ROLE_ADMIN")

                .antMatchers("/api/visit/{visitId}").hasAuthority("ROLE_CLIENT")
                .antMatchers("/api/visit/get-worker-available-days-in-month").hasAuthority("ROLE_CLIENT")
                .antMatchers("/api/visit/get-available-worker-terms-for-day").hasAuthority("ROLE_CLIENT")
                .antMatchers("/api/visit/all-as-client-by-user-id/{userId}").hasAuthority("ROLE_WORKER")
                .antMatchers("/api/visit/all-of-unregistered-client/{unregisteredClientId}").hasAuthority("ROLE_WORKER")
                .antMatchers("/api/visit/get-week-not-cancelled-visits-as-worker/{workerId}").hasAuthority("ROLE_WORKER")
                .antMatchers("/api/visit/get-week-not-cancelled-visits-as-client/{workerId}").hasAuthority("ROLE_WORKER")
                .antMatchers("/api/visit/get-week-not-cancelled-item-visit/{itemId}").hasAuthority("ROLE_WORKER")
                .antMatchers("/api/visit/{visitId}/mark-as-done").hasAuthority("ROLE_WORKER")
                .antMatchers("/api/visit/{visitId}/mark-as-paid").hasAuthority("ROLE_WORKER")
                .antMatchers("/api/visit/mark-as-deleted/{visitId}").hasAuthority("ROLE_WORKER")
                .antMatchers("/api/visit/{visitId}/cancel").hasAuthority("ROLE_CLIENT")
                .antMatchers("/api/visit/cancel-all-of-unregistered-client/{unregisteredClientId}").hasAuthority("ROLE_RECEPTIONIST")
                .antMatchers("/api/visit/cancel-all-as-client-by-user-id/{userId}").hasAuthority("ROLE_RECEPTIONIST")
                .antMatchers("/api/visit/cancel-all-as-worker-by-user-id/{userId}").hasAuthority("ROLE_RECEPTIONIST")

                .antMatchers("/api/worker/find-by-service/{serviceId}").hasAuthority("ROLE_CLIENT")
                .antMatchers("/api/worker/find-clients/{workerId}").hasAuthority("ROLE_WORKER")
                .antMatchers("/api/worker/find-unregistered-clients/{workerId}").hasAuthority("ROLE_WORKER")
                .antMatchers("/api/worker/workerServices/{id}").hasAuthority("ROLE_CLIENT")
                .antMatchers("/api/worker/assignServicesToWorker/{id}").hasAuthority("ROLE_ADMIN")
                .antMatchers("/api/worker/get-week-work-hours/{workerId}").hasAuthority("ROLE_WORKER")
                .antMatchers("/api/worker/get-week-off-work-hours/{workerId}").hasAuthority("ROLE_WORKER")
                .antMatchers("/api/worker/get-week-absence-hours/{workerId}").hasAuthority("ROLE_WORKER")
                .antMatchers("/api/worker/set-work-hours/{workerId}").hasAuthority("ROLE_RECEPTIONIST")
                .antMatchers("/api/worker/set-absence-hours/{workerId}").hasAuthority("ROLE_RECEPTIONIST")
                .antMatchers("/api/worker/delete-daily-absence-hours/{workerId}").hasAuthority("ROLE_RECEPTIONIST")
                .antMatchers("/api/worker/delete-daily-work-hours/{workerId}").hasAuthority("ROLE_RECEPTIONIST")

                .anyRequest().authenticated();
    }
}
