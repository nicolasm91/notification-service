package org.modak.notificationservice.rates;

import org.modak.notificationservice.model.NotificationRate;
import org.modak.notificationservice.model.NotificationType;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

@Component
public class NotificationRatesConfigImpl implements NotificationRatesConfig {

    public Map<NotificationType, NotificationRate> getNotificationRates(){
        return Map.of(
                NotificationType.STATUS, new NotificationRate(2, Duration.ofMinutes(1)),
                NotificationType.MARKETING, new NotificationRate(1, Duration.ofDays(1)),
                NotificationType.NEWS, new NotificationRate(3, Duration.ofHours(3)));
    }
}
