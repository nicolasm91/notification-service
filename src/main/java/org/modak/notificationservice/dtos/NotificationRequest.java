package org.modak.notificationservice.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.modak.notificationservice.model.NotificationType;

@Value
@Builder
@AllArgsConstructor
public class NotificationRequest {
    @NotNull NotificationType type;
    @NotEmpty String userId;
    @NotEmpty String body;
}
