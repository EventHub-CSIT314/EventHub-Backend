package com.eventorganizer.user.repository;

import com.eventorganizer.user.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

//    Finds a UserProfile by its username.
    Optional<UserProfile> findByUserName(String username);

//    Finds a UserProfile by its email.
    Optional<UserProfile> findByUserEmail(String email);

//    Checks if a UserProfile exists by its userName.
    boolean existsByUserName(String userName);

//    Checks if a UserProfile exists by its userEmail.
    boolean existsByUserEmail(String userEmail);
} 