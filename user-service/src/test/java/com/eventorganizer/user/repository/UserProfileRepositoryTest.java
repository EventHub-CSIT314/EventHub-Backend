package com.eventorganizer.user.repository;

import com.eventorganizer.user.model.UserProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserProfileRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Test
    void findByUserName_WhenUserExists_ReturnsUserProfile() {
        // Arrange
        UserProfile userProfile = UserProfile.builder()
                .userName("testuser")
                .firstName("John")
                .lastName("Doe")
                .userEmail("john.doe@example.com")
                .build();
        entityManager.persist(userProfile);
        entityManager.flush();

        // Act
        Optional<UserProfile> found = userProfileRepository.findByUserName("testuser");

        // Assert
        assertTrue(found.isPresent());
        assertEquals("testuser", found.get().getUserName());
        assertEquals("John", found.get().getFirstName());
        assertEquals("Doe", found.get().getLastName());
        assertEquals("john.doe@example.com", found.get().getUserEmail());
    }

    @Test
    void findByUserName_WhenUserDoesNotExist_ReturnsEmpty() {
        // Act
        Optional<UserProfile> found = userProfileRepository.findByUserName("nonexistent");

        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    void findByUserEmail_WhenUserExists_ReturnsUserProfile() {
        // Arrange
        UserProfile userProfile = UserProfile.builder()
                .userName("testuser")
                .firstName("John")
                .lastName("Doe")
                .userEmail("john.doe@example.com")
                .build();
        entityManager.persist(userProfile);
        entityManager.flush();

        // Act
        Optional<UserProfile> found = userProfileRepository.findByUserEmail("john.doe@example.com");

        // Assert
        assertTrue(found.isPresent());
        assertEquals("testuser", found.get().getUserName());
        assertEquals("john.doe@example.com", found.get().getUserEmail());
    }

    @Test
    void findByUserEmail_WhenUserDoesNotExist_ReturnsEmpty() {
        // Act
        Optional<UserProfile> found = userProfileRepository.findByUserEmail("nonexistent@example.com");

        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    void existsByUsername_WhenUserExists_ReturnsTrue() {
        // Arrange
        UserProfile userProfile = UserProfile.builder()
                .userName("testuser")
                .firstName("John")
                .lastName("Doe")
                .userEmail("john.doe@example.com")
                .build();
        entityManager.persist(userProfile);
        entityManager.flush();

        // Act
        boolean exists = userProfileRepository.existsByUserName("testuser");

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsByUserName_WhenUserDoesNotExist_ReturnsFalse() {
        // Act
        boolean exists = userProfileRepository.existsByUserName("nonexistent");

        // Assert
        assertFalse(exists);
    }

    @Test
    void existsByEmail_WhenUserExists_ReturnsTrueUser() {
        // Arrange
        UserProfile userProfile = UserProfile.builder()
                .userName("testuser")
                .firstName("John")
                .lastName("Doe")
                .userEmail("john.doe@example.com")
                .build();
        entityManager.persist(userProfile);
        entityManager.flush();

        // Act
        boolean exists = userProfileRepository.existsByUserEmail("john.doe@example.com");

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsByUserEmail_WhenUserDoesNotExist_ReturnsFalse() {
        // Act
        boolean exists = userProfileRepository.existsByUserEmail("nonexistent@example.com");

        // Assert
        assertFalse(exists);
    }
} 