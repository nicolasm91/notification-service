package org.modak.notificationservice.model;

import lombok.Data;

import java.time.Duration;

@Data
public class NotificationRate {
    private int allowedRate;
    private Duration timeFrame;

    public NotificationRate(int allowedRate, Duration timeFrame) {
        this.allowedRate = allowedRate;
        this.timeFrame = timeFrame;
    }
}
