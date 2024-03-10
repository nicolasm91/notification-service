package org.modak.notificationservice.repositories;

import org.modak.notificationservice.model.Notification;
import org.modak.notificationservice.model.NotificationType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationsRepository extends CrudRepository<Notification, UUID> {
    List<Notification> findAByUserIdAndType(String userId, NotificationType type);
}
