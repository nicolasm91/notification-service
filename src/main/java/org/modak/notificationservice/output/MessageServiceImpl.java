package org.modak.notificationservice.output;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {
    @Override
    public void send(String type, String userId, String message) throws RuntimeException {
        // TODO: Define implementation details for this integration
        log.info("Message sent - type: {} | user-id: {} | message: {}", type, userId, message);
    }
}
