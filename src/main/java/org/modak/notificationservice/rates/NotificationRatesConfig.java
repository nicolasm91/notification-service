package org.modak.notificationservice.rates;

import org.modak.notificationservice.model.NotificationRate;
import org.modak.notificationservice.model.NotificationType;

import java.util.Map;

public interface NotificationRatesConfig {
    Map<NotificationType, NotificationRate> getNotificationRates();
}
