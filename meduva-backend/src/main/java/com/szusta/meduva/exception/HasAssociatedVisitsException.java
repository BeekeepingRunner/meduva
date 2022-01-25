package com.szusta.meduva.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class HasAssociatedVisitsException extends RuntimeException {

    public static final long serialVersionUID = 1L;

    public HasAssociatedVisitsException(String message) {
        super(message);
    }
}
