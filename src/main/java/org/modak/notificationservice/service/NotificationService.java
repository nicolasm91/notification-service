package org.modak.notificationservice.service;

import lombok.extern.slf4j.Slf4j;
import org.modak.notificationservice.repositories.NotificationsCache;
import org.modak.notificationservice.exceptions.ExceededRateException;
import org.modak.notificationservice.model.Notification;
import org.modak.notificationservice.model.NotificationRate;
import org.modak.notificationservice.dtos.NotificationRequest;
import org.modak.notificationservice.model.NotificationType;
import org.modak.notificationservice.rates.NotificationRatesConfig;
import org.modak.notificationservice.utils.RequestValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class NotificationService {

    private final NotificationsCache notificationsCache;
    private final NotificationRatesConfig notificationRatesConfig;

    public NotificationService(NotificationsCache notificationsCache,
                               NotificationRatesConfig notificationRatesConfig) {
        this.notificationsCache = notificationsCache;
        this.notificationRatesConfig = notificationRatesConfig;
    }

    public ResponseEntity<?> process(NotificationRequest request) {

        log.info("POST -- Request: " + request.toString());

        RequestValidator.validateRequest(request);

        Map<NotificationType, NotificationRate> ratesByType = notificationRatesConfig.getNotificationRates();

        if (!isNotificationWithinAllowedRateRule(request, ratesByType))
            throw new ExceededRateException("Too many requests of type {" + request.getType() + "} for userId {" + request.getUserId() +"}");

        Notification notification = Notification.builder()
                .uuid(UUID.randomUUID())
                .timestamp(LocalDateTime.now())
                .userId(request.getUserId())
                .type(request.getType())
                .body(request.getBody())
                .build();

        notificationsCache.save(notification);
        return ResponseEntity.ok(notification);
    }

    public boolean isNotificationWithinAllowedRateRule(NotificationRequest request, Map<NotificationType, NotificationRate> businessRules) {
        long count = notificationsCache.findAByUserIdAndType(request.getUserId(), request.getType())
                .stream()
                .filter(notification -> isIncludedWithinTimeRange(notification, businessRules.get(request.getType()).getTimeFrame()))
                .count();

        return count < businessRules.get(request.getType()).getAllowedRate();
    }

    private static boolean isIncludedWithinTimeRange(Notification notification, Duration timeFrame) {
        return Duration.between(notification.getTimestamp(), LocalDateTime.now())
                .compareTo(timeFrame) < 0;
    }

}
