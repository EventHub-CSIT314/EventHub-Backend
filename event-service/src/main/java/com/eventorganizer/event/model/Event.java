package com.eventorganizer.event.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class  Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventID;

    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    private String eventName;

    @Column(length = 1000)
    private String eventDescription;

    @NotNull(message = "Date and time is required")
    @FutureOrPresent(message = "Event date must be in the present or future")
    @Column(name = "date_time", nullable = false)
    private LocalDateTime eventDateTime;

    @NotBlank(message = "Location is required")
    @Column(nullable = false)
    private String eventLocation;

    @NotBlank(message = "Category is required")
    @Column(nullable = false)
    private String eventCategory;

    @PositiveOrZero(message = "Price must be zero or positive")
    @Column(nullable = false)
    private double eventPrice;

    @NotNull(message = "Organizer ID is required")
    @Positive(message = "Organizer ID must be positive")
    @Column(name = "organizer_id", nullable = false)
    private Long organizerID;

    @NotBlank(message = "Status is required")
    @Column(nullable = false)
    @Builder.Default
    private String eventStatus = "ACTIVE";

    @Column(name = "created_at")
    private LocalDateTime eventCreate_ts;

    @Column(name = "updated_at")
    private LocalDateTime eventUpdate_ts;

    @PrePersist
    public void onCreate() {
        eventCreate_ts = LocalDateTime.now();
        eventUpdate_ts = eventCreate_ts;
    }

    @PreUpdate
    public void onUpdate() {
        eventUpdate_ts = LocalDateTime.now();
    }
} 