package com.szusta.meduva.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class RoleNotFoundException extends RuntimeException {

    public static final long serialVersionUID = 1L;

    public RoleNotFoundException(String message) {
        super(message);
    }
}
