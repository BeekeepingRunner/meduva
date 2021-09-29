package com.szusta.meduva.exception.notfound;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmailResetTokenNotFoundException extends RuntimeException {

    public static final long serialVersionUID = 1L;

    public EmailResetTokenNotFoundException(String message){ super(message);}
}
