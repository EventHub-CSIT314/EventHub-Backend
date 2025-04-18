package com.eventorganizer.registration.repository;

import com.eventorganizer.registration.RegistrationServiceApplication;
import com.eventorganizer.registration.model.Registration;
import com.eventorganizer.registration.model.RegistrationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = RegistrationServiceApplication.class)
@ActiveProfiles("test")
class RegistrationRepositoryTest {

    @Autowired
    private RegistrationRepository registrationRepository;

    private Registration testRegistration;

    @BeforeEach
    void setUp() {
        registrationRepository.deleteAll();
        
        testRegistration = new Registration();
        testRegistration.setEventID(1L);
        testRegistration.setUserID(1L);
        testRegistration.setRegistrationDate(LocalDateTime.now());
        testRegistration.setStatus(RegistrationStatus.REGISTERED);
        testRegistration.setTicketToken("test-token-123");
        
        testRegistration = registrationRepository.save(testRegistration);
    }

    @Test
    void save_ValidRegistration_ShouldSucceed() {
        Registration newRegistration = new Registration();
        newRegistration.setEventID(2L);
        newRegistration.setUserID(2L);
        newRegistration.setRegistrationDate(LocalDateTime.now());
        newRegistration.setStatus(RegistrationStatus.REGISTERED);
        newRegistration.setTicketToken("new-token-456");
        
        Registration savedRegistration = registrationRepository.save(newRegistration);
        
        assertNotNull(savedRegistration.getRegID());
        assertEquals(newRegistration.getEventID(), savedRegistration.getEventID());
        assertEquals(newRegistration.getUserID(), savedRegistration.getUserID());
        assertEquals(newRegistration.getStatus(), savedRegistration.getStatus());
        assertEquals(newRegistration.getTicketToken(), savedRegistration.getTicketToken());
    }

    @Test
    void findById_ExistingRegistration_ShouldReturnRegistration() {
        Optional<Registration> foundRegistration = registrationRepository.findById(testRegistration.getRegID());
        
        assertTrue(foundRegistration.isPresent());
        assertEquals(testRegistration.getRegID(), foundRegistration.get().getRegID());
        assertEquals(testRegistration.getEventID(), foundRegistration.get().getEventID());
        assertEquals(testRegistration.getUserID(), foundRegistration.get().getUserID());
        assertEquals(testRegistration.getStatus(), foundRegistration.get().getStatus());
        assertEquals(testRegistration.getTicketToken(), foundRegistration.get().getTicketToken());
    }

    @Test
    void findById_NonExistingRegistration_ShouldReturnEmpty() {
        Optional<Registration> foundRegistration = registrationRepository.findById(999L);
        
        assertFalse(foundRegistration.isPresent());
    }

    @Test
    void findByUserID_ExistingRegistrations_ShouldReturnRegistrations() {
        // Create another registration for the same user
        Registration anotherRegistration = new Registration();
        anotherRegistration.setEventID(3L);
        anotherRegistration.setUserID(1L); // Same user ID
        anotherRegistration.setRegistrationDate(LocalDateTime.now());
        anotherRegistration.setStatus(RegistrationStatus.REGISTERED);
        anotherRegistration.setTicketToken("another-token-789");
        registrationRepository.save(anotherRegistration);
        
        List<Registration> userRegistrations = registrationRepository.findByUserID(1L);
        
        assertEquals(2, userRegistrations.size());
        assertTrue(userRegistrations.stream().allMatch(r -> r.getUserID().equals(1L)));
    }

    @Test
    void findByUserID_NonExistingRegistrations_ShouldReturnEmptyList() {
        List<Registration> userRegistrations = registrationRepository.findByUserID(999L);
        
        assertTrue(userRegistrations.isEmpty());
    }

    @Test
    void findByEventID_ExistingRegistrations_ShouldReturnRegistrations() {
        // Create another registration for the same event
        Registration anotherRegistration = new Registration();
        anotherRegistration.setEventID(1L); // Same event ID
        anotherRegistration.setUserID(2L);
        anotherRegistration.setRegistrationDate(LocalDateTime.now());
        anotherRegistration.setStatus(RegistrationStatus.REGISTERED);
        anotherRegistration.setTicketToken("another-token-789");
        registrationRepository.save(anotherRegistration);
        
        List<Registration> eventRegistrations = registrationRepository.findByEventID(1L);
        
        assertEquals(2, eventRegistrations.size());
        assertTrue(eventRegistrations.stream().allMatch(r -> r.getEventID().equals(1L)));
    }

    @Test
    void findByEventID_NonExistingRegistrations_ShouldReturnEmptyList() {
        List<Registration> eventRegistrations = registrationRepository.findByEventID(999L);
        
        assertTrue(eventRegistrations.isEmpty());
    }

    @Test
    void findByStatus_ExistingRegistrations_ShouldReturnRegistrations() {
        // Create a cancelled registration
        Registration cancelledRegistration = new Registration();
        cancelledRegistration.setEventID(3L);
        cancelledRegistration.setUserID(3L);
        cancelledRegistration.setRegistrationDate(LocalDateTime.now());
        cancelledRegistration.setStatus(RegistrationStatus.CANCELLED);
        cancelledRegistration.setTicketToken("cancelled-token-789");
        registrationRepository.save(cancelledRegistration);
        
        List<Registration> registeredRegistrations = registrationRepository.findByStatus(RegistrationStatus.REGISTERED);
        List<Registration> cancelledRegistrations = registrationRepository.findByStatus(RegistrationStatus.CANCELLED);
        
        assertEquals(1, registeredRegistrations.size());
        assertEquals(1, cancelledRegistrations.size());
        assertTrue(registeredRegistrations.stream().allMatch(r -> r.getStatus() == RegistrationStatus.REGISTERED));
        assertTrue(cancelledRegistrations.stream().allMatch(r -> r.getStatus() == RegistrationStatus.CANCELLED));
    }

    @Test
    void findByStatus_NonExistingRegistrations_ShouldReturnEmptyList() {
        List<Registration> registrations = registrationRepository.findByStatus(RegistrationStatus.CANCELLED);
        
        assertTrue(registrations.isEmpty());
    }
} 