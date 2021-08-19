package com.szusta.meduva.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserNotFoundException extends RuntimeException {

    public static final long serialVersionUID = 1L;

    public UserNotFoundException(String message) {
        super(message);
    }
}
