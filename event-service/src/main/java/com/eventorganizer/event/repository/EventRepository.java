package com.eventorganizer.event.repository;

import com.eventorganizer.event.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByEventCategory(String category);
    List<Event> findByEventLocationAndEventDateTimeBetween(String location, LocalDateTime startDate, LocalDateTime endDate);
    List<Event> findByOrganizerID(Long organizerID);
    List<Event> findByEventStatus(String eventStatus);

//    NOT NECESSARY FOR NOW
//    List<Event> findByPriceLessThanEqual(double price);

    List<Event> findByEventDateTimeGreaterThanEqual(LocalDateTime dateTime);
} 