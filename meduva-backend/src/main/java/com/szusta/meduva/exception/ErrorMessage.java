package com.szusta.meduva.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class ErrorMessage {

    int httpStatus;
    Date date;
    String message;
    String description;
}
