package org.modak.notificationservice.exceptions;

import org.springframework.http.HttpStatus;

public class ExceededRateException extends ExceptionWrapper {

    public ExceededRateException(String message) {
        super(HttpStatus.TOO_MANY_REQUESTS, message);
    }
}
