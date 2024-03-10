package org.modak.notificationservice.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public abstract class ExceptionWrapper extends RuntimeException {
    @Getter
    private final HttpStatus status;
    private final String message;

    public ExceptionWrapper(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
