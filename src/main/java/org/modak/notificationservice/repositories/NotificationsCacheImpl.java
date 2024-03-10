package org.modak.notificationservice.repositories;

import org.modak.notificationservice.model.Notification;
import org.modak.notificationservice.model.NotificationType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationsCacheImpl implements NotificationsCache {

    private final NotificationsRepository repository;

    public NotificationsCacheImpl(NotificationsRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Notification> findAByUserIdAndType(String userId, NotificationType type) {
        return repository.findAByUserIdAndType(userId,type);
    }

    @Override
    public void save(Notification notification) {
        repository.save(notification);
    }
}
