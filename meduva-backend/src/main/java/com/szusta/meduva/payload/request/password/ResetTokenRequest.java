package com.szusta.meduva.payload.request.password;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetTokenRequest {

    private String token;
}
