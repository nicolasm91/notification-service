package org.modak.notificationservice.model;

import lombok.Getter;

@Getter
public enum NotificationType {
    STATUS("STATUS"),
    NEWS("NEWS"),
    MARKETING("MARKETING");

    private final String value;

    NotificationType(String value) {
        this.value = value;
    }

}
