package com.eventorganizer.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userProfileID;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String userEmail;

    private String userPhone;

// NECESSARY?
//    @Column(length = 1000)
//    private String bio;

    @Column(name = "profile_picture_url")
    private String profilePictureURL;

    @Column(name = "created_at")
    private LocalDateTime create_ts;

    @Column(name = "updated_at")
    private LocalDateTime update_ts;

    @PrePersist
    public void onCreate() {
        create_ts = LocalDateTime.now();
        update_ts = create_ts;
    }

    @PreUpdate
    public void onUpdate() {
        update_ts = LocalDateTime.now();
    }
} 