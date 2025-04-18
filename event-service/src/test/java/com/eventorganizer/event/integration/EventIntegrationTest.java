package com.eventorganizer.event.integration;

import com.eventorganizer.event.model.Event;
import com.eventorganizer.event.repository.EventRepository;
import com.eventorganizer.event.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class EventIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    private Event testEvent;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        // Clear the database before each test
        eventRepository.deleteAll();
        
        now = LocalDateTime.now();
        
        // Create a test event
        testEvent = new Event();
        testEvent.setEventName("Integration Test Event");
        testEvent.setEventDescription("Test Description");
        testEvent.setEventDateTime(now.plusDays(1));
        testEvent.setEventLocat("Test Location");
        testEvent.setEventCategory("CONFERENCE");
        testEvent.setEventPrice(99.99);
        testEvent.setOrganizerID(1L);
        testEvent.setEventStatus("ACTIVE");
        
        // Save the test event to the database
        testEvent = eventRepository.save(testEvent);
    }

    @Test
    void createEvent_ValidEvent_ReturnsCreated() throws Exception {
        // Create a new event
        Event newEvent = new Event();
        newEvent.setEventName("New Integration Test Event");
        newEvent.setEventDescription("New Test Description");
        newEvent.setEventDateTime(now.plusDays(2));
        newEvent.setEventLocat("New Test Location");
        newEvent.setEventCategory("WORKSHOP");
        newEvent.setEventPrice(149.99);
        newEvent.setOrganizerID(2L);
        newEvent.setEventStatus("ACTIVE");

        // Perform the POST request
        String response = mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(newEvent.getEventName()))
                .andExpect(jsonPath("$.description").value(newEvent.getEventDescription()))
                .andExpect(jsonPath("$.location").value(newEvent.getEventLocat()))
                .andExpect(jsonPath("$.category").value(newEvent.getEventCategory()))
                .andExpect(jsonPath("$.price").value(newEvent.getEventPrice()))
                .andExpect(jsonPath("$.organizerId").value(newEvent.getOrganizerID()))
                .andExpect(jsonPath("$.status").value(newEvent.getEventStatus()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Parse the response
        Event createdEvent = objectMapper.readValue(response, Event.class);

        // Verify the event was saved to the database
        Event savedEvent = eventRepository.findById(createdEvent.getEventID()).orElse(null);
        assertNotNull(savedEvent);
        assertEquals(newEvent.getEventName(), savedEvent.getEventName());
        assertEquals(newEvent.getEventDescription(), savedEvent.getEventDescription());
        assertEquals(newEvent.getEventLocat(), savedEvent.getEventLocat());
        assertEquals(newEvent.getEventCategory(), savedEvent.getEventCategory());
        assertEquals(newEvent.getEventPrice(), savedEvent.getEventPrice());
        assertEquals(newEvent.getOrganizerID(), savedEvent.getOrganizerID());
        assertEquals(newEvent.getEventStatus(), savedEvent.getEventStatus());
    }

    @Test
    void getEvent_ExistingEvent_ReturnsEvent() throws Exception {
        // Perform the GET request
        mockMvc.perform(get("/api/v1/events/{id}", testEvent.getEventID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testEvent.getEventID()))
                .andExpect(jsonPath("$.title").value(testEvent.getEventName()))
                .andExpect(jsonPath("$.description").value(testEvent.getEventDescription()))
                .andExpect(jsonPath("$.location").value(testEvent.getEventLocat()))
                .andExpect(jsonPath("$.category").value(testEvent.getEventCategory()))
                .andExpect(jsonPath("$.price").value(testEvent.getEventPrice()))
                .andExpect(jsonPath("$.organizerId").value(testEvent.getOrganizerID()))
                .andExpect(jsonPath("$.status").value(testEvent.getEventStatus()));
    }

    @Test
    void updateEvent_Authorized_UpdatesEvent() throws Exception {
        // Create an updated event
        Event updatedEvent = new Event();
        updatedEvent.setEventName("Updated Integration Test Event");
        updatedEvent.setEventDescription("Updated Test Description");
        updatedEvent.setEventDateTime(now.plusDays(3));
        updatedEvent.setEventLocat("Updated Test Location");
        updatedEvent.setEventCategory("SEMINAR");
        updatedEvent.setEventPrice(199.99);
        updatedEvent.setOrganizerID(testEvent.getOrganizerID());
        updatedEvent.setEventStatus("ACTIVE");

        // Perform the PUT request
        mockMvc.perform(put("/api/v1/events/{id}", testEvent.getEventID())
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Organizer-Id", testEvent.getOrganizerID())
                .content(objectMapper.writeValueAsString(updatedEvent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testEvent.getEventID()))
                .andExpect(jsonPath("$.title").value(updatedEvent.getEventName()))
                .andExpect(jsonPath("$.description").value(updatedEvent.getEventDescription()))
                .andExpect(jsonPath("$.location").value(updatedEvent.getEventLocat()))
                .andExpect(jsonPath("$.category").value(updatedEvent.getEventCategory()))
                .andExpect(jsonPath("$.price").value(updatedEvent.getEventPrice()));

        // Verify the event was updated in the database
        Event savedEvent = eventRepository.findById(testEvent.getEventID()).orElse(null);
        assertNotNull(savedEvent);
        assertEquals(updatedEvent.getEventName(), savedEvent.getEventName());
        assertEquals(updatedEvent.getEventDescription(), savedEvent.getEventDescription());
        assertEquals(updatedEvent.getEventLocat(), savedEvent.getEventLocat());
        assertEquals(updatedEvent.getEventCategory(), savedEvent.getEventCategory());
        assertEquals(updatedEvent.getEventPrice(), savedEvent.getEventPrice());
    }

    @Test
    void cancelEvent_Authorized_CancelsEvent() throws Exception {
        // Perform the DELETE request
        mockMvc.perform(delete("/api/v1/events/{id}", testEvent.getEventID())
                .header("X-Organizer-Id", testEvent.getOrganizerID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testEvent.getEventID()))
                .andExpect(jsonPath("$.status").value("CANCELLED"));

        // Verify the event was cancelled in the database
        Event savedEvent = eventRepository.findById(testEvent.getEventID()).orElse(null);
        assertNotNull(savedEvent);
        assertEquals("CANCELLED", savedEvent.getEventStatus());
    }

    @Test
    void findEventsByCategory_ReturnsFilteredEvents() throws Exception {
        // Create additional events with different categories
        Event workshopEvent = new Event();
        workshopEvent.setEventName("Workshop Event");
        workshopEvent.setEventDescription("Workshop Description");
        workshopEvent.setEventDateTime(now.plusDays(2));
        workshopEvent.setEventLocat("Workshop Location");
        workshopEvent.setEventCategory("WORKSHOP");
        workshopEvent.setEventPrice(149.99);
        workshopEvent.setOrganizerID(1L);
        workshopEvent.setEventStatus("ACTIVE");
        eventRepository.save(workshopEvent);

        // Perform the GET request with category filter
        mockMvc.perform(get("/api/v1/events")
                .param("category", "CONFERENCE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testEvent.getEventID()))
                .andExpect(jsonPath("$[0].category").value("CONFERENCE"));

        // Verify the correct events were returned
        List<Event> conferenceEvents = eventRepository.findByEventCategory("CONFERENCE");
        assertEquals(1, conferenceEvents.size());
        assertEquals("CONFERENCE", conferenceEvents.get(0).getEventCategory());
    }

    @Test
    void findUpcomingEvents_ReturnsFutureEvents() throws Exception {
        // Create another future event with a later date
        Event laterEvent = new Event();
        laterEvent.setEventName("Later Event");
        laterEvent.setEventDescription("Later Description");
        laterEvent.setEventDateTime(now.plusDays(2));
        laterEvent.setEventLocat("Later Location");
        laterEvent.setEventCategory("CONFERENCE");
        laterEvent.setEventPrice(99.99);
        laterEvent.setOrganizerID(1L);
        laterEvent.setEventStatus("ACTIVE");
        eventRepository.save(laterEvent);

        // Perform the GET request for upcoming events
        mockMvc.perform(get("/api/v1/events/upcoming"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id").exists())
                .andExpect(jsonPath("$[*].title").exists())
                .andExpect(jsonPath("$[*].dateTime").exists());

        // Verify all events are in the future
        List<Event> upcomingEvents = eventRepository.findByEventDateTimeGreaterThanEqual(now);
        assertTrue(upcomingEvents.size() >= 2);
        for (Event event : upcomingEvents) {
            assertTrue(event.getEventDateTime().isAfter(now) || event.getEventDateTime().isEqual(now));
        }
    }
} 