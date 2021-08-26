package com.szusta.meduva.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeRequest {

    private String resetToken;
    private String newPassword;
}
