package org.modak.notificationservice.utils;

import org.modak.notificationservice.dtos.NotificationRequest;
import org.modak.notificationservice.exceptions.InvalidRequestException;

import java.util.Optional;

public class RequestValidator {

    public static void validateRequest(NotificationRequest request) {
        Optional.ofNullable(request.getUserId()).orElseThrow(() -> new InvalidRequestException("'userId' field cannot be empty nor null"));
        Optional.ofNullable(request.getType()).orElseThrow(() -> new InvalidRequestException("'type' field cannot be empty nor null"));
        Optional.ofNullable(request.getBody()).orElseThrow(() -> new InvalidRequestException("'body' field cannot be empty nor null"));
    }
}
