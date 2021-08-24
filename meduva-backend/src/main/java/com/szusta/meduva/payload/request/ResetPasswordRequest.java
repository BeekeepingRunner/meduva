package com.szusta.meduva.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {

    private String token;
    private String password;
    private String repeatPassword;
}
