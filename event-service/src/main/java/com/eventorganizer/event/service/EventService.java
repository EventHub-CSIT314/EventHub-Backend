package com.eventorganizer.event.service;

import com.eventorganizer.event.model.Event;
import com.eventorganizer.event.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event createEvent(Event event) {
        if (event.getEventDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Event date must be in the future");
        }
        return eventRepository.save(event);
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
    }

    public Event updateEvent(Long eventID, Event updatedEvent, Long organizerID) {
        Event event = getEventById(eventID);
        if (!event.getOrganizerID().equals(organizerID)) {
            throw new RuntimeException("Not authorized to update this event");
        }
        
        event.setEventName(updatedEvent.getEventName());
        event.setEventDescription(updatedEvent.getEventDescription());
        event.setEventDateTime(updatedEvent.getEventDateTime());
        event.setEventLocation(updatedEvent.getEventLocation());
        event.setEventCategory(updatedEvent.getEventCategory());
        event.setEventPrice(updatedEvent.getEventPrice());
        event.setEventStatus(updatedEvent.getEventStatus());
        
        return eventRepository.save(event);
    }

    public Event cancelEvent(Long eventID, Long organizerID) {
        Event event = getEventById(eventID);
        if (!event.getOrganizerID().equals(organizerID)) {
            throw new RuntimeException("Not authorized to cancel this event");
        }
        event.setEventStatus("CANCELLED");
        return eventRepository.save(event);
    }

    public List<Event> findEventsByCategory(String category) {
        return eventRepository.findByEventCategory(category);
    }

    public List<Event> findEventsByLocation(String location, LocalDateTime startDate, LocalDateTime endDate) {
        return eventRepository.findByLocationAndDateTimeBetween(location, startDate, endDate);
    }

    public List<Event> findEventsByOrganizer(Long organizerID) {
        return eventRepository.findByOrganizerID(organizerID);
    }

    public List<Event> findUpcomingEvents() {
        return eventRepository.findByDateTimeGreaterThanEqual(LocalDateTime.now());
    }
} 