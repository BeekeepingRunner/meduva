package com.szusta.meduva.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SignupRequest {

    private String email;
    private String login;
    private String password;
    private String name;
    private String surname;
    private String phoneNumber;

    private Set<String> role;
}
