package com.eventorganizer.event.event;

import com.eventorganizer.event.event.UserProfileEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j // Better logging with SLF4J
public class UserProfileEventConsumer {

    @KafkaListener(topics = "user-profile-events", groupId = "auth-service")
    public void consume(UserProfileEvent event) {
        // Handle the event based on eventType
        switch (event.getEventType()) {
            case "UPDATED":
                log.info("User profile updated: {}", event.getUserName());  // Better logging
                break;
            case "DELETED":
                log.info("User profile deleted: {}", event.getUserName());
                break;
            case "GET":
                log.info("User profile get: {}", event.getUserName());
                break;
            default:
                log.warn("Unknown event type: {}", event.getEventType());
        }
    }
}