package com.turborvip.security.application.configuration.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serial;


@Getter
@Setter
public class InvalidException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    /*
    to ensure version consistency when traversing objects between versions of an application or between different applications
    When the Serializable object is dispatched, the serialVersionUID is appended to the object's data to identify the version of the class.
     */

    private HttpStatus status;

    private String userMessage;

    private String devMessage;

    public InvalidException(String userMessage, String devMessage) {
        super(userMessage);
        this.status = HttpStatus.BAD_REQUEST;
        this.userMessage = userMessage;
        this.devMessage = devMessage;
    }

    public InvalidException(HttpStatus status, String userMessage, String devMessage) {
        super(userMessage);
        this.status = status;
        this.userMessage = userMessage;
        this.devMessage = devMessage;
    }
}
