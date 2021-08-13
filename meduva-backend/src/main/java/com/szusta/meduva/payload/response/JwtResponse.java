package com.szusta.meduva.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class JwtResponse {

    private String token;
    private String type = "Bearer ";
    private Long id;
    private String login;
    private String email;
    private List<String> roles;

    public JwtResponse(String token, Long id, String login, String email, List<String> roles) {
        this.token = token;
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

    public String getUsername() {
        return login;
    }

    public void setUsername(String username) {
        this.login = username;
    }

    public List<String> getRoles() {
        return roles;
    }
}
