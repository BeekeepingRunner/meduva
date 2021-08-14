package com.szusta.meduva.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class LoginAlreadyTakenException extends RuntimeException {

    public static final long serialVersionUID = 1L;

    public LoginAlreadyTakenException(String message) {
        super(message);
    }
}
