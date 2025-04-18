package com.eventorganizer.event.repository;

import com.eventorganizer.event.config.TestConfig;
import com.eventorganizer.event.model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestConfig.class)
@ActiveProfiles("test")
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    private Event testEvent;

    @BeforeEach
    void setUp() {
        // Create a test event before each test
        testEvent = new Event();
        testEvent.setEventName("Test Event");
        testEvent.setEventDescription("Test Description");
        testEvent.setEventDateTime(LocalDateTime.now().plusDays(1));
        testEvent.setEventLocat("Test Location");
        testEvent.setEventCategory("CONFERENCE");
        testEvent.setEventPrice(99.99);
        testEvent.setOrganizerID(1L);
        testEvent.setEventStatus("ACTIVE");
        
        // Save the test event
        testEvent = eventRepository.save(testEvent);
    }

    @Test
    void findById_WhenEventExists_ReturnsEvent() {
        // Act
        Optional<Event> foundEvent = eventRepository.findById(testEvent.getEventID());

        // Assert
        assertTrue(foundEvent.isPresent());
        assertEquals(testEvent.getEventName(), foundEvent.get().getEventName());
        assertEquals(testEvent.getEventDescription(), foundEvent.get().getEventDescription());
        assertEquals(testEvent.getEventLocat(), foundEvent.get().getEventLocat());
        assertEquals(testEvent.getEventCategory(), foundEvent.get().getEventCategory());
        assertEquals(testEvent.getEventPrice(), foundEvent.get().getEventPrice());
        assertEquals(testEvent.getOrganizerID(), foundEvent.get().getOrganizerID());
        assertEquals(testEvent.getEventStatus(), foundEvent.get().getEventStatus());
    }

    @Test
    void findById_WhenEventDoesNotExist_ReturnsEmpty() {
        // Act
        Optional<Event> foundEvent = eventRepository.findById(999L);

        // Assert
        assertFalse(foundEvent.isPresent());
    }

    @Test
    void findByCategory_WhenEventsExist_ReturnsEvents() {
        // Arrange
        Event workshopEvent = new Event();
        workshopEvent.setEventName("Workshop Event");
        workshopEvent.setEventDescription("Workshop Description");
        workshopEvent.setEventDateTime(LocalDateTime.now().plusDays(2));
        workshopEvent.setEventLocat("Workshop Location");
        workshopEvent.setEventCategory("WORKSHOP");
        workshopEvent.setEventPrice(149.99);
        workshopEvent.setOrganizerID(1L);
        workshopEvent.setEventStatus("ACTIVE");
        eventRepository.save(workshopEvent);

        // Act
        List<Event> conferenceEvents = eventRepository.findByCategory("CONFERENCE");
        List<Event> workshopEvents = eventRepository.findByCategory("WORKSHOP");

        // Assert
        assertEquals(1, conferenceEvents.size());
        assertEquals(1, workshopEvents.size());
        assertEquals("Test Event", conferenceEvents.get(0).getEventName());
        assertEquals("Workshop Event", workshopEvents.get(0).getEventName());
    }

    @Test
    void findByOrganizerId_WhenEventsExist_ReturnsEvents() {
        // Arrange
        Event secondEvent = new Event();
        secondEvent.setEventName("Second Event");
        secondEvent.setEventDescription("Second Description");
        secondEvent.setEventDateTime(LocalDateTime.now().plusDays(3));
        secondEvent.setEventLocat("Second Location");
        secondEvent.setEventCategory("CONFERENCE");
        secondEvent.setEventPrice(199.99);
        secondEvent.setOrganizerID(1L);
        secondEvent.setEventStatus("ACTIVE");
        eventRepository.save(secondEvent);

        // Act
        List<Event> organizerEvents = eventRepository.findByOrganizerId(1L);

        // Assert
        assertEquals(2, organizerEvents.size());
    }

    @Test
    void findByStatus_WhenEventsExist_ReturnsEvents() {
        // Arrange
        Event cancelledEvent = new Event();
        cancelledEvent.setEventName("Cancelled Event");
        cancelledEvent.setEventDescription("Cancelled Description");
        cancelledEvent.setEventDateTime(LocalDateTime.now().plusDays(4));
        cancelledEvent.setEventLocat("Cancelled Location");
        cancelledEvent.setEventCategory("CONFERENCE");
        cancelledEvent.setEventPrice(299.99);
        cancelledEvent.setOrganizerID(1L);
        cancelledEvent.setEventStatus("CANCELLED");
        eventRepository.save(cancelledEvent);

        // Act
        List<Event> activeEvents = eventRepository.findByStatus("ACTIVE");
        List<Event> cancelledEvents = eventRepository.findByStatus("CANCELLED");

        // Assert
        assertEquals(1, activeEvents.size());
        assertEquals(1, cancelledEvents.size());
        assertEquals("Test Event", activeEvents.get(0).getEventName());
        assertEquals("Cancelled Event", cancelledEvents.get(0).getEventName());
    }

    @Test
    void findByDateTimeGreaterThanEqual_WhenFutureEventsExist_ReturnsEvents() {
        // Arrange
        Event pastEvent = new Event();
        pastEvent.setEventName("Past Event");
        pastEvent.setEventDescription("Past Description");
        pastEvent.setEventDateTime(LocalDateTime.now().plusDays(1));
        pastEvent.setEventLocat("Past Location");
        pastEvent.setEventCategory("CONFERENCE");
        pastEvent.setEventPrice(399.99);
        pastEvent.setOrganizerID(1L);
        pastEvent.setEventStatus("ACTIVE");
        eventRepository.save(pastEvent);

        // Act
        List<Event> futureEvents = eventRepository.findByDateTimeGreaterThanEqual(LocalDateTime.now());

        // Assert
        assertEquals(2, futureEvents.size());
        assertTrue(futureEvents.stream().anyMatch(e -> e.getEventName().equals("Test Event")));
        assertTrue(futureEvents.stream().anyMatch(e -> e.getEventName().equals("Past Event")));
    }
} 