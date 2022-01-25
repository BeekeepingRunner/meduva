package com.szusta.meduva.unit.jwt;

import com.szusta.meduva.model.role.Role;
import com.szusta.meduva.model.User;
import com.szusta.meduva.security.jwt.JwtUtils;
import com.szusta.meduva.service.user.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilsTest {

    private User user;
    private UserDetailsImpl userDetails;
    String simpleToken;
    private final JwtUtils jwtUtils = new JwtUtils();

    @BeforeEach
    void setUp() {
        jwtUtils.setJwtSecret("secret");
        jwtUtils.setJwtExpirationMs(3600000);

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

        userDetails = UserDetailsImpl.build(user);

        simpleToken = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .signWith(SignatureAlgorithm.HS256, jwtUtils.getJwtSecret())
                .compact();
    }

    @Test
    public void validateTokenTest() {

        assertTrue(jwtUtils.validateJwtToken(simpleToken));
        assertFalse(jwtUtils.validateJwtToken("lol"));
    }

    @Test
    public void getUserNameFromJwtTest() {

        assertEquals(
                userDetails.getUsername(),
                jwtUtils.getUserNameFromJwt(simpleToken));
    }

    @Test
    public void generateJwtTokenTest() {

        String jwt = jwtUtils.generateJwtTokenFrom(userDetails);
        assertTrue(jwtUtils.validateJwtToken(jwt));
    }
}
