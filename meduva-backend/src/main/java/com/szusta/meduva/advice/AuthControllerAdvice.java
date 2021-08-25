package com.szusta.meduva.advice;

import com.szusta.meduva.exception.*;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @ExceptionHandler(value = BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMessage handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {

        return new ErrorMessage(
                HttpStatus.UNAUTHORIZED.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
    }

    @ExceptionHandler(value = LoginAlreadyTakenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleLoginAlreadyTakenException(LoginAlreadyTakenException ex, WebRequest request) {

        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
    }

    @ExceptionHandler(value = EmailAlreadyInUseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleEmailAlreadyInUseException(EmailAlreadyInUseException ex, WebRequest request) {

        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
    }

    @ExceptionHandler(value = BadRequestRole.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleBadRequestRoleException(BadRequestRole ex, WebRequest request) {

        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<Object> handleNotValidArguments(MethodArgumentNotValidException ex, WebRequest request) {

        final List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        final List<ErrorMessage> customFieldErrors = new ArrayList<>();

        for (FieldError fieldError : fieldErrors) {

            final ErrorMessage customFieldError = ErrorMessage.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST.value())
                    .date(new Date())
                    .message(fieldError.getDefaultMessage())
                    .description(request.getDescription(false))
                    .build();

            customFieldErrors.add(customFieldError);
        }
        return ResponseEntity.badRequest().body(customFieldErrors);
    }

}
