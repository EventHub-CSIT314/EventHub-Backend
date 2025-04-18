package com.eventorganizer.registration.repository;

import com.eventorganizer.registration.model.Registration;
import com.eventorganizer.registration.model.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByUserID(Long userID);
    List<Registration> findByEventID(Long eventID);
    List<Registration> findByStatus(RegistrationStatus status);
} 