package com.turborvip.security.application.configuration.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BadRequestException extends RuntimeException{

    private String message;

    private static final HttpStatus status = HttpStatus.BAD_REQUEST;

    public BadRequestException(String message) {
        this.message = message;
    }
}
