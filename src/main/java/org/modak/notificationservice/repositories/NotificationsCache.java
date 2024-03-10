package org.modak.notificationservice.repositories;

import org.modak.notificationservice.model.Notification;
import org.modak.notificationservice.model.NotificationType;

import java.util.List;

public interface NotificationsCache {

    List<Notification> findAByUserIdAndType(String userId, NotificationType type);

    void save(Notification notification);
}
