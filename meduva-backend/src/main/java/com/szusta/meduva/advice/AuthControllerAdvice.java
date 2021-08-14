package com.szusta.meduva.advice;

import com.szusta.meduva.exception.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class AuthControllerAdvice {

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorMessage handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, WebRequest request) {

        return new ErrorMessage(
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
    }
}
