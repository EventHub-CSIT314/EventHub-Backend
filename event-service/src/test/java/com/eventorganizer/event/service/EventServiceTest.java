package com.eventorganizer.event.service;

import com.eventorganizer.event.model.Event;
import com.eventorganizer.event.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    private Event event;
    private static final Long EVENT_ID = 1L;
    private static final Long ORGANIZER_ID = 1L;

    @BeforeEach
    void setUp() {
        event = Event.builder()
                .eventID(EVENT_ID)
                .eventName("Tech Conference 2024")
                .eventDescription("Annual technology conference")
                .eventDateTime(LocalDateTime.now().plusDays(1))
                .eventLocation("Convention Center")
                .eventCategory("CONFERENCE")
                .eventPrice(99.99)
                .organizerID(ORGANIZER_ID)
                .eventStatus("ACTIVE")
                .build();
    }

    @Test
    void createEvent_WithValidData_Success() {
        // Arrange
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        // Act
        Event created = eventService.createEvent(event);

        // Assert
        assertNotNull(created);
        assertEquals(EVENT_ID, created.getEventID());
        assertEquals("Tech Conference 2024", created.getEventName());
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void createEvent_WithPastDate_ThrowsException() {
        // Arrange
        Event pastEvent = Event.builder()
                .eventName("Past Event")
                .eventDateTime(LocalDateTime.now().minusDays(1))
                .eventLocation("Venue")
                .eventCategory("CONFERENCE")
                .organizerID(ORGANIZER_ID)
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> eventService.createEvent(pastEvent));
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void getEventById_WhenEventExists_ReturnsEvent() {
        // Arrange
        when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(event));

        // Act
        Event found = eventService.getEventById(EVENT_ID);

        // Assert
        assertNotNull(found);
        assertEquals(EVENT_ID, found.getEventID());
        assertEquals("Tech Conference 2024", found.getEventName());
        verify(eventRepository).findById(EVENT_ID);
    }

    @Test
    void getEventById_WhenEventDoesNotExist_ThrowsException() {
        // Arrange
        when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> eventService.getEventById(EVENT_ID));
        verify(eventRepository).findById(EVENT_ID);
    }

    @Test
    void updateEvent_WhenEventExistsAndOrganizerValid_UpdatesEvent() {
        // Arrange
        Event updatedEvent = Event.builder()
                .eventID(EVENT_ID)
                .eventName("Updated Conference 2024")
                .eventDescription("Updated description")
                .eventDateTime(LocalDateTime.now().plusDays(2))
                .eventLocation("New Venue")
                .eventCategory("CONFERENCE")
                .eventPrice(149.99)
                .organizerID(ORGANIZER_ID)
                .eventStatus("ACTIVE")
                .build();

        when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(updatedEvent);

        // Act
        Event result = eventService.updateEvent(EVENT_ID, updatedEvent, ORGANIZER_ID);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Conference 2024", result.getEventName());
        assertEquals("Updated description", result.getEventDescription());
        assertEquals(149.99, result.getEventPrice());
        verify(eventRepository).findById(EVENT_ID);
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void updateEvent_WhenNotOrganizer_ThrowsException() {
        // Arrange
        Long differentOrganizerId = 2L;
        when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(event));

        // Act & Assert
        assertThrows(RuntimeException.class, 
            () -> eventService.updateEvent(EVENT_ID, event, differentOrganizerId));
        verify(eventRepository).findById(EVENT_ID);
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void cancelEvent_WhenEventExistsAndOrganizerValid_CancelsEvent() {
        // Arrange
        when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Event cancelled = eventService.cancelEvent(EVENT_ID, ORGANIZER_ID);

        // Assert
        assertNotNull(cancelled);
        assertEquals("CANCELLED", cancelled.getEventStatus());
        verify(eventRepository).findById(EVENT_ID);
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void cancelEvent_WhenNotOrganizer_ThrowsException() {
        // Arrange
        Long differentOrganizerId = 2L;
        when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(event));

        // Act & Assert
        assertThrows(RuntimeException.class, 
            () -> eventService.cancelEvent(EVENT_ID, differentOrganizerId));
        verify(eventRepository).findById(EVENT_ID);
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void findEventsByCategory_WhenEventsExist_ReturnsEvents() {
        // Arrange
        List<Event> events = Arrays.asList(event);
        when(eventRepository.findByEventCategory("CONFERENCE")).thenReturn(events);

        // Act
        List<Event> found = eventService.findEventsByCategory("CONFERENCE");

        // Assert
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals("Tech Conference 2024", found.get(0).getEventName());
        verify(eventRepository).findByEventCategory("CONFERENCE");
    }

    @Test
    void findEventsByLocation_WhenEventsExist_ReturnsEvents() {
        // Arrange
        List<Event> events = Arrays.asList(event);
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(7);
        
        when(eventRepository.findByEventLocationAndEventDateTimeBetween("Convention Center", start, end))
                .thenReturn(events);

        // Act
        List<Event> found = eventService.findEventsByLocation("Convention Center", start, end);

        // Assert
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals("Tech Conference 2024", found.get(0).getEventName());
        verify(eventRepository).findByEventLocationAndEventDateTimeBetween("Convention Center", start, end);
    }

    @Test
    void findEventsByOrganizer_WhenEventsExist_ReturnsEvents() {
        // Arrange
        List<Event> events = Arrays.asList(event);
        when(eventRepository.findByOrganizerID(ORGANIZER_ID)).thenReturn(events);

        // Act
        List<Event> found = eventService.findEventsByOrganizer(ORGANIZER_ID);

        // Assert
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals("Tech Conference 2024", found.get(0).getEventName());
        verify(eventRepository).findByOrganizerID(ORGANIZER_ID);
    }

    @Test
    void findUpcomingEvents_WhenEventsExist_ReturnsEvents() {
        // Arrange
        List<Event> events = Arrays.asList(event);
        when(eventRepository.findByEventDateTimeGreaterThanEqual(any(LocalDateTime.class)))
                .thenReturn(events);

        // Act
        List<Event> found = eventService.findUpcomingEvents();

        // Assert
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals("Tech Conference 2024", found.get(0).getEventName());
        verify(eventRepository).findByEventDateTimeGreaterThanEqual(any(LocalDateTime.class));
    }
} 