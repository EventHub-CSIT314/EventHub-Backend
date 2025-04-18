package com.eventorganizer.event.mapper;

import com.eventorganizer.event.dto.EventDTO;
import com.eventorganizer.event.model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EventMapperTest {

    private EventMapper eventMapper;
    private Event event;
    private EventDTO eventDTO;
    private static final Long ID = 1L;
    private static final String TITLE = "Tech Conference 2024";
    private static final String DESCRIPTION = "Annual technology conference";
    private static final LocalDateTime DATE_TIME = LocalDateTime.now().plusDays(1);
    private static final String LOCATION = "Convention Center";
    private static final String CATEGORY = "CONFERENCE";
    private static final double PRICE = 99.99;
    private static final Long ORGANIZER_ID = 1L;
    private static final String STATUS = "ACTIVE";

    @BeforeEach
    void setUp() {
        eventMapper = new EventMapper();
        
        // Create a test Event
        event = Event.builder()
                .eventID(ID)
                .eventName(TITLE)
                .eventDescription(DESCRIPTION)
                .eventDateTime(DATE_TIME)
                .eventLocat(LOCATION)
                .category(CATEGORY)
                .price(PRICE)
                .organizerId(ORGANIZER_ID)
                .status(STATUS)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Create a test EventDTO
        eventDTO = EventDTO.builder()
                .id(ID)
                .title(TITLE)
                .description(DESCRIPTION)
                .dateTime(DATE_TIME)
                .location(LOCATION)
                .category(CATEGORY)
                .price(PRICE)
                .organizerId(ORGANIZER_ID)
                .status(STATUS)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void toDTO_WhenEventIsNull_ReturnsNull() {
        assertNull(eventMapper.toDTO(null));
    }

    @Test
    void toDTO_WhenEventIsValid_ReturnsCorrectDTO() {
        EventDTO result = eventMapper.toDTO(event);

        assertNotNull(result);
        assertEquals(ID, result.getId());
        assertEquals(TITLE, result.getTitle());
        assertEquals(DESCRIPTION, result.getDescription());
        assertEquals(DATE_TIME, result.getDateTime());
        assertEquals(LOCATION, result.getLocation());
        assertEquals(CATEGORY, result.getCategory());
        assertEquals(PRICE, result.getPrice());
        assertEquals(ORGANIZER_ID, result.getOrganizerId());
        assertEquals(STATUS, result.getStatus());
    }

    @Test
    void toEntity_WhenDTOIsNull_ReturnsNull() {
        assertNull(eventMapper.toEntity(null));
    }

    @Test
    void toEntity_WhenDTOIsValid_ReturnsCorrectEntity() {
        Event result = eventMapper.toEntity(eventDTO);

        assertNotNull(result);
        assertEquals(ID, result.getEventID());
        assertEquals(TITLE, result.getEventName());
        assertEquals(DESCRIPTION, result.getEventDescription());
        assertEquals(DATE_TIME, result.getEventDateTime());
        assertEquals(LOCATION, result.getEventLocat());
        assertEquals(CATEGORY, result.getEventCategory());
        assertEquals(PRICE, result.getEventPrice());
        assertEquals(ORGANIZER_ID, result.getOrganizerID());
        assertEquals(STATUS, result.getEventStatus());
    }

    @Test
    void updateEventFromDTO_WhenDTOIsNull_DoesNotModifyEvent() {
        Event originalEvent = Event.builder()
                .eventID(ID)
                .eventName(TITLE)
                .eventDescription(DESCRIPTION)
                .eventDateTime(DATE_TIME)
                .eventLocat(LOCATION)
                .category(CATEGORY)
                .price(PRICE)
                .organizerId(ORGANIZER_ID)
                .status(STATUS)
                .build();

        eventMapper.updateEventFromDTO(originalEvent, null);

        assertEquals(TITLE, originalEvent.getEventName());
        assertEquals(DESCRIPTION, originalEvent.getEventDescription());
        assertEquals(DATE_TIME, originalEvent.getEventDateTime());
        assertEquals(LOCATION, originalEvent.getEventLocat());
        assertEquals(CATEGORY, originalEvent.getEventCategory());
        assertEquals(PRICE, originalEvent.getEventPrice());
        assertEquals(STATUS, originalEvent.getEventStatus());
    }

    @Test
    void updateEventFromDTO_WhenDTOHasPartialUpdates_UpdatesOnlyProvidedFields() {
        Event originalEvent = Event.builder()
                .eventID(ID)
                .eventName(TITLE)
                .eventDescription(DESCRIPTION)
                .eventDateTime(DATE_TIME)
                .eventLocat(LOCATION)
                .category(CATEGORY)
                .price(PRICE)
                .organizerId(ORGANIZER_ID)
                .status(STATUS)
                .build();

        EventDTO partialUpdate = EventDTO.builder()
                .title("Updated Title")
                .price(149.99)
                .status("CANCELLED")
                .build();

        eventMapper.updateEventFromDTO(originalEvent, partialUpdate);

        assertEquals("Updated Title", originalEvent.getEventName());
        assertEquals(DESCRIPTION, originalEvent.getEventDescription()); // Should remain unchanged
        assertEquals(DATE_TIME, originalEvent.getEventDateTime()); // Should remain unchanged
        assertEquals(LOCATION, originalEvent.getEventLocat()); // Should remain unchanged
        assertEquals(CATEGORY, originalEvent.getEventCategory()); // Should remain unchanged
        assertEquals(149.99, originalEvent.getEventPrice());
        assertEquals("CANCELLED", originalEvent.getEventStatus());
    }
} 