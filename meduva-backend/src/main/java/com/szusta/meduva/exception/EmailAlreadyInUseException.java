package com.szusta.meduva.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class EmailAlreadyInUseException extends RuntimeException {

    public static final long serialVersionUID = 1L;

    public EmailAlreadyInUseException(String message) {
        super(message);
    }
}
