package com.szusta.meduva.payload.request.password;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ResetPasswordRequest {

    @NotNull
    private String resetToken;

    @NotNull
    @Size(min = 8, max = 20, message = "Password must contain between 8 and 20 characters")
    @Pattern(regexp = "^[^-\\t\\r\\n\\v\\f]+$", message = "Password must not contain forbidden characters")
    private String password;

    @NotNull
    @Size(min = 8, max = 20, message = "Password must contain between 8 and 20 characters")
    @Pattern(regexp = "^[^-\\t\\r\\n\\v\\f]+$", message = "Password must not contain forbidden characters")
    private String repeatPassword;
}
