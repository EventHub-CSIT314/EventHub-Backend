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
                .eventID(event.getEventID())
                .eventName(event.getEventName())
                .eventDescription(event.getEventDescription())
                .eventDateTime(event.getEventDateTime())
                .eventLocation(event.getEventLocation())
                .eventCategory(event.getEventCategory())
                .eventPrice(event.getEventPrice())
                .organizerID(event.getOrganizerID())
                .eventStatus(event.getEventStatus())
                .eventCreate_ts(event.getEventCreate_ts())
                .eventUpdate_ts(event.getEventUpdate_ts())
                .build();
    }

    public Event toEntity(EventDTO eventDTO) {
        if (eventDTO == null) {
            return null;
        }

        return Event.builder()
                .eventID(eventDTO.getEventID())
                .eventName(eventDTO.getEventName())
                .eventDescription(eventDTO.getEventDescription())
                .eventDateTime(eventDTO.getEventDateTime())
                .eventLocation(eventDTO.getEventLocation())
                .eventCategory(eventDTO.getEventCategory())
                .eventPrice(eventDTO.getEventPrice())
                .organizerID(eventDTO.getOrganizerID())
                .eventStatus(eventDTO.getEventStatus())
                .build();
    }

    public void updateEventFromDTO(Event event, EventDTO eventDTO) {
        if (eventDTO == null) {
            return;
        }

        if (eventDTO.getEventName() != null) {
            event.setEventName(eventDTO.getEventName());
        }
        if (eventDTO.getEventDescription() != null) {
            event.setEventDescription(eventDTO.getEventDescription());
        }
        if (eventDTO.getEventDateTime() != null) {
            event.setEventDateTime(eventDTO.getEventDateTime());
        }
        if (eventDTO.getEventLocation() != null) {
            event.setEventLocation(eventDTO.getEventLocation());
        }
        if (eventDTO.getEventCategory() != null) {
            event.setEventCategory(eventDTO.getEventCategory());
        }
        if (eventDTO.getEventPrice() != null) {
            event.setEventPrice(eventDTO.getEventPrice());
        }
        if (eventDTO.getEventStatus() != null) {
            event.setEventStatus(eventDTO.getEventStatus());
        }
    }
} 