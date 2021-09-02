package com.szusta.meduva.payload.response;

import com.szusta.meduva.model.Role;

import java.util.Set;

public class JwtResponse {

    private String token;
    private String type = "Bearer ";
    private String refreshToken;
    private Long id;
    private String login;
    private String email;
    private Set<Role> roles;

    public JwtResponse(String token,
                       String refreshToken,
                       Long id,
                       String login,
                       String email,
                       Set<Role> roles) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.id = id;
        this.login = login;
        this.email = email;
        this.roles = roles;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String username) {
        this.login = username;
    }

    public Set<Role> getRoles() {
        return roles;
    }
}
