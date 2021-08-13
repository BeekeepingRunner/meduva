package com.szusta.meduva.payload.request;


import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SignupRequest {

    private String login;
    private String email;

    private Set<String> role;

    private String password;
}
