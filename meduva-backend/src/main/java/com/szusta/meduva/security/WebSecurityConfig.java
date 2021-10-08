package com.szusta.meduva.security;

import com.szusta.meduva.filter.AuthTokenFilter;
import com.szusta.meduva.security.jwt.AuthEntryPointJwt;
import com.szusta.meduva.service.UserDetailsServiceImpl;
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

    // TODO: finish permitting endpoint access for users with specific roles
    private void authorizeRequests(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/greetings").permitAll()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/test/all").permitAll()
                .antMatchers("/api/password/request").permitAll()
                .antMatchers("/api/password/user").permitAll()
                .antMatchers("/api/password/validate-reset-token").permitAll()
                .antMatchers("/api/password/change").permitAll()
                .antMatchers("/api/service/{id}").hasAuthority("ROLE_ADMIN")
                .antMatchers("/services").hasAuthority("ROLE_ADMIN")
                .antMatchers("/api/user/edit/{id}").hasAuthority("ROLE_CLIENT")
                .antMatchers("/api/email/request/{id}").hasAuthority("ROLE_CLIENT")
                .antMatchers("/api/email/validate-email-reset-token").permitAll()
                .anyRequest().authenticated();
        //.anyRequest().permitAll();
    }
}
