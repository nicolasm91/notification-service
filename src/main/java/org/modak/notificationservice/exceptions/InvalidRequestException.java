package org.modak.notificationservice.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidRequestException extends ExceptionWrapper {

    public InvalidRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
