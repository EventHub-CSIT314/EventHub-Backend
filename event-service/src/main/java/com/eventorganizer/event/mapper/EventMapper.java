package com.eventorganizer.event.mapper;

import com.eventorganizer.event.dto.EventDTO;
import com.eventorganizer.event.model.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public EventDTO toDTO(Event event) {
        if (event == null) {
            return null;
        }

        return EventDTO.builder()
                .id(event.getEventID())
                .title(event.getEventName())
                .description(event.getEventDescription())
                .dateTime(event.getEventDateTime())
                .location(event.getEventLocat())
                .category(event.getEventCategory())
                .price(event.getEventPrice())
                .organizerId(event.getOrganizerID())
                .status(event.getEventStatus())
                .createdAt(event.getEventCreate_ts())
                .updatedAt(event.getEventUpdate_ts())
                .build();
    }

    public Event toEntity(EventDTO eventDTO) {
        if (eventDTO == null) {
            return null;
        }

        return Event.builder()
                .eventID(eventDTO.getId())
                .eventName(eventDTO.getTitle())
                .eventDescription(eventDTO.getDescription())
                .eventDateTime(eventDTO.getDateTime())
                .eventLocat(eventDTO.getLocation())
                .category(eventDTO.getCategory())
                .price(eventDTO.getPrice())
                .organizerId(eventDTO.getOrganizerId())
                .status(eventDTO.getStatus())
                .build();
    }

    public void updateEventFromDTO(Event event, EventDTO eventDTO) {
        if (eventDTO == null) {
            return;
        }

        if (eventDTO.getTitle() != null) {
            event.setEventName(eventDTO.getTitle());
        }
        if (eventDTO.getDescription() != null) {
            event.setEventDescription(eventDTO.getDescription());
        }
        if (eventDTO.getDateTime() != null) {
            event.setEventDateTime(eventDTO.getDateTime());
        }
        if (eventDTO.getLocation() != null) {
            event.setEventLocat(eventDTO.getLocation());
        }
        if (eventDTO.getCategory() != null) {
            event.setEventCategory(eventDTO.getCategory());
        }
        if (eventDTO.getPrice() != null) {
            event.setEventPrice(eventDTO.getPrice());
        }
        if (eventDTO.getStatus() != null) {
            event.setEventStatus(eventDTO.getStatus());
        }
    }
} 