package com.eventorganizer.event.event;

import com.eventorganizer.event.event.UserProfileEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserProfileEventConsumer {

    @KafkaListener(topics = "user-profile-events", groupId = "auth-service")
    public void consume(UserProfileEvent event) {
        // Handle the event based on eventType
        switch (event.getEventType()) {
            case "UPDATED":
                // Update local user data
                System.out.println("User profile updated: " + event.getUserName());
                break;
            case "DELETED":
                // Handle user deletion
                System.out.println("User profile deleted: " + event.getUserName());
                break;
        }
    }
}