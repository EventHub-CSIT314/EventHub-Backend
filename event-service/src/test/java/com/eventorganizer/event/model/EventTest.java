package com.eventorganizer.event.model;

import com.eventorganizer.event.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestConfig.class)
@ActiveProfiles("test")
class EventTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    void testEventPersistence() {
        // Create a new event
        Event event = new Event();
        event.setEventName("Test Event");
        event.setEventDescription("Test Description");
        event.setEventDateTime(LocalDateTime.now().plusDays(1));
        event.setEventLocat("Test Location");
        event.setEventCategory("CONFERENCE");
        event.setEventPrice(99.99);
        event.setOrganizerID(1L);
        event.setEventStatus("ACTIVE");

        // Persist the event
        entityManager.persist(event);
        entityManager.flush();
        entityManager.clear();

        // Retrieve the event
        Event foundEvent = entityManager.find(Event.class, event.getEventID());

        // Assert the event was persisted correctly
        assertNotNull(foundEvent);
        assertEquals("Test Event", foundEvent.getEventName());
        assertEquals("Test Description", foundEvent.getEventDescription());
        assertEquals("Test Location", foundEvent.getEventLocat());
        assertEquals("CONFERENCE", foundEvent.getEventCategory());
        assertEquals(99.99, foundEvent.getEventPrice());
        assertEquals(1L, foundEvent.getOrganizerID());
        assertEquals("ACTIVE", foundEvent.getEventStatus());
    }

    @Test
    void testEventUpdate() {
        // Create and persist an event
        Event event = new Event();
        event.setEventName("Original Title");
        event.setEventDescription("Original Description");
        event.setEventDateTime(LocalDateTime.now().plusDays(1));
        event.setEventLocat("Original Location");
        event.setEventCategory("CONFERENCE");
        event.setEventPrice(99.99);
        event.setOrganizerID(1L);
        event.setEventStatus("ACTIVE");

        entityManager.persist(event);
        entityManager.flush();
        entityManager.clear();

        // Update the event
        Event foundEvent = entityManager.find(Event.class, event.getEventID());
        foundEvent.setEventName("Updated Title");
        foundEvent.setEventDescription("Updated Description");
        foundEvent.setEventPrice(149.99);
        
        entityManager.merge(foundEvent);
        entityManager.flush();
        entityManager.clear();

        // Retrieve the updated event
        Event updatedEvent = entityManager.find(Event.class, event.getEventID());

        // Assert the event was updated correctly
        assertNotNull(updatedEvent);
        assertEquals("Updated Title", updatedEvent.getEventName());
        assertEquals("Updated Description", updatedEvent.getEventDescription());
        assertEquals(149.99, updatedEvent.getEventPrice());
    }

    @Test
    void testEventDeletion() {
        // Create and persist an event
        Event event = new Event();
        event.setEventName("Event to Delete");
        event.setEventDescription("Description");
        event.setEventDateTime(LocalDateTime.now().plusDays(1));
        event.setEventLocat("Location");
        event.setEventCategory("CONFERENCE");
        event.setEventPrice(99.99);
        event.setOrganizerID(1L);
        event.setEventStatus("ACTIVE");

        entityManager.persist(event);
        entityManager.flush();
        entityManager.clear();

        // Delete the event
        Event foundEvent = entityManager.find(Event.class, event.getEventID());
        entityManager.remove(foundEvent);
        entityManager.flush();
        entityManager.clear();

        // Try to retrieve the deleted event
        Event deletedEvent = entityManager.find(Event.class, event.getEventID());

        // Assert the event was deleted
        assertNull(deletedEvent);
    }

    @Test
    void testEventQuery() {
        // Create and persist multiple events
        Event event1 = new Event();
        event1.setEventName("Event 1");
        event1.setEventDescription("Description 1");
        event1.setEventDateTime(LocalDateTime.now().plusDays(1));
        event1.setEventLocat("Location 1");
        event1.setEventCategory("CONFERENCE");
        event1.setEventPrice(99.99);
        event1.setOrganizerID(1L);
        event1.setEventStatus("ACTIVE");

        Event event2 = new Event();
        event2.setEventName("Event 2");
        event2.setEventDescription("Description 2");
        event2.setEventDateTime(LocalDateTime.now().plusDays(2));
        event2.setEventLocat("Location 2");
        event2.setEventCategory("WORKSHOP");
        event2.setEventPrice(149.99);
        event2.setOrganizerID(1L);
        event2.setEventStatus("ACTIVE");

        entityManager.persist(event1);
        entityManager.persist(event2);
        entityManager.flush();
        entityManager.clear();

        // Query events by category
        List<Event> conferenceEvents = entityManager.createQuery(
                "SELECT e FROM Event e WHERE e.category = :category", Event.class)
                .setParameter("category", "CONFERENCE")
                .getResultList();

        // Assert the query results
        assertEquals(1, conferenceEvents.size());
        assertEquals("Event 1", conferenceEvents.get(0).getEventName());
    }
} 