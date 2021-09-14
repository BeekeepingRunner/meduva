package com.szusta.meduva.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ServiceAlreadyExistsException extends RuntimeException {

    public static final long serialVersionUID = 1L;

    public ServiceAlreadyExistsException(String message) {
        super(message);
    }
}
