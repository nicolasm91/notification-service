package org.modak.notificationservice.output;

public interface MessageService {

    void send(String type, String userId, String message) throws RuntimeException;
}
