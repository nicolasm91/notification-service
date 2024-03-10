package org.modak.notificationservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("modak-notifications:user-notification")
public class Notification {
    @Id
    private UUID uuid;
    @Indexed
    LocalDateTime timestamp;
    @Indexed
    NotificationType type;
    @Indexed
    String userId;
    String body;
}
