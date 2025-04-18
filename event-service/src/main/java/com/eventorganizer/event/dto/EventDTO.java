package com.eventorganizer.event.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private Long eventID;

    @NotBlank(message = "Title is required")
    private String eventName;

    private String eventDescription;

    @NotNull(message = "Date and time are required")
    @Future(message = "Event date must be in the future")
    private LocalDateTime eventDateTime;

    @NotBlank(message = "Location is required")
    private String eventLocation;

    @NotBlank(message = "Category is required")
    private String eventCategory;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Double eventPrice;

    private Long organizerID;
    private String eventStatus;
    private LocalDateTime eventCreate_ts;
    private LocalDateTime eventUpdate_ts;
} 