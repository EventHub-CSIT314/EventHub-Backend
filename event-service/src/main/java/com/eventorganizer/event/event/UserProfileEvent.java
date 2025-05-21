package com.eventorganizer.event.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileEvent {
    private String eventType; // e.g., "CREATE", "UPDATE", "DELETE"
    private String userName;
    private String userEmail;
    private String firstName;
    private String lastName;
}
