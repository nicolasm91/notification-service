package org.modak.notificationservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modak.notificationservice.dtos.NotificationRequest;
import org.modak.notificationservice.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/notifications")
public class NotificationsController {

    private final NotificationService service;
    public NotificationsController(NotificationService notificationService) {
        this.service = notificationService;
    }

    @Operation(summary = "Send a notification to a user")
    @PostMapping("/")
    public ResponseEntity<?> handleNotification(@Valid @RequestBody NotificationRequest request) {
        return service.process(request);
    }
}
