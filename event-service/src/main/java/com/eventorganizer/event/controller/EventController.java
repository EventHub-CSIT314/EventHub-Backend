package com.eventorganizer.event.controller;

import com.eventorganizer.event.model.Event;
import com.eventorganizer.event.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Event event) {
        try {
            Event createdEvent = eventService.createEvent(event);
            return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{eventID}")
    public ResponseEntity<Event> getEvent(@PathVariable Long eventID) {
        try {
            Event event = eventService.getEventById(eventID);
            return ResponseEntity.ok(event);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{eventID}")
    public ResponseEntity<Event> updateEvent(
            @PathVariable Long eventID,
            @Valid @RequestBody Event event,
            @RequestHeader("X-Organizer-Id") Long organizerID) {
        try {
            Event updatedEvent = eventService.updateEvent(eventID, event, organizerID);
            return ResponseEntity.ok(updatedEvent);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Not authorized")) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{eventID}")
    public ResponseEntity<Event> cancelEvent(
            @PathVariable Long eventID,
            @RequestHeader("X-Organizer-Id") Long organizerId) {
        try {
            Event cancelledEvent = eventService.cancelEvent(eventID, organizerId);
            return ResponseEntity.ok(cancelledEvent);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Not authorized")) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Event>> findEvents(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date must be after start date");
        }
        
        if (category != null) {
            return ResponseEntity.ok(eventService.findEventsByCategory(category));
        }
        
        if (location != null && startDate != null && endDate != null) {
            return ResponseEntity.ok(eventService.findEventsByLocation(location, startDate, endDate));
        }
        
        return ResponseEntity.ok(eventService.findUpcomingEvents());
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<Event>> findUpcomingEvents() {
        return ResponseEntity.ok(eventService.findUpcomingEvents());
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Event Service is running");
    }

    @GetMapping("/{eventID}/exists")
    public ResponseEntity<Boolean> eventExists(@PathVariable Long eventID) {
        try {
            eventService.getEventById(eventID);
            return ResponseEntity.ok(true);
        } catch (RuntimeException e) {
            return ResponseEntity.ok(false);
        }
    }
} 