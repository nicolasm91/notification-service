package org.modak.notificationservice.model;

public enum NotificationType {
    STATUS("STATUS"),
    NEWS("NEWS"),
    MARKETING("MARKETING");

    private String value;

    NotificationType(String value) {
        this.value = value;
    }
}
