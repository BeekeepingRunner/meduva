package com.szusta.meduva.advice;

import com.szusta.meduva.exception.EntityRecordNotFoundException;
import com.szusta.meduva.exception.ErrorMessage;
import com.szusta.meduva.exception.HasAssociatedVisitsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class DBEntityControllerAdvice {

    @ExceptionHandler(value = EntityRecordNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleEntityRecordNotFoundException(EntityRecordNotFoundException ex, WebRequest request) {

        return new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
    }

    @ExceptionHandler(value = HasAssociatedVisitsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleHasAssociatedVisitsException(HasAssociatedVisitsException ex, WebRequest request) {

        return new ErrorMessage(
                HttpStatus.PRECONDITION_FAILED.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
    }
}
