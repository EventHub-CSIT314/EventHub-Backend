package com.eventorganizer.user.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserProfileTest {

    @Test
    void createUserProfile_WithValidData_Success() {
        // Arrange
        UserProfile userProfile = UserProfile.builder()
                .userProfileID(1L)
                .userName("testuser")
                .firstName("John")
                .lastName("Doe")
                .userEmail("john.doe@example.com")
                .userPhone("1234567890")
                .profilePictureURL("http://example.com/profile.jpg")
                .build();

        // Assert
        assertNotNull(userProfile);
        assertEquals(1L, userProfile.getUserProfileID());
        assertEquals("testuser", userProfile.getUserName());
        assertEquals("John", userProfile.getFirstName());
        assertEquals("Doe", userProfile.getLastName());
        assertEquals("john.doe@example.com", userProfile.getUserEmail());
        assertEquals("1234567890", userProfile.getUserPhone());
        assertEquals("http://example.com/profile.jpg", userProfile.getProfilePictureURL());
    }

    @Test
    void createUserProfile_WithMinimalData_Success() {
        // Arrange
        UserProfile userProfile = UserProfile.builder()
                .userName("testuser")
                .firstName("John")
                .lastName("Doe")
                .userEmail("john.doe@example.com")
                .build();

        // Assert
        assertNotNull(userProfile);
        assertNull(userProfile.getUserProfileID());
        assertEquals("testuser", userProfile.getUserName());
        assertEquals("John", userProfile.getFirstName());
        assertEquals("Doe", userProfile.getLastName());
        assertEquals("john.doe@example.com", userProfile.getUserEmail());
        assertNull(userProfile.getUserPhone());
        assertNull(userProfile.getProfilePictureURL());
    }

    @Test
    void equalsAndHashCode_WithSameObjects_ReturnsTrue() {
        // Arrange
        UserProfile userProfile1 = UserProfile.builder()
                .userProfileID(1L)
                .userName("testuser")
                .firstName("John")
                .lastName("Doe")
                .userEmail("john.doe@example.com")
                .build();

        UserProfile userProfile2 = UserProfile.builder()
                .userProfileID(1L)
                .userName("testuser")
                .firstName("John")
                .lastName("Doe")
                .userEmail("john.doe@example.com")
                .build();

        // Assert
        assertEquals(userProfile1, userProfile2);
        assertEquals(userProfile1.hashCode(), userProfile2.hashCode());
    }

    @Test
    void equalsAndHashCode_WithDifferentObjects_ReturnsFalse() {
        // Arrange
        UserProfile userProfile1 = UserProfile.builder()
                .userProfileID(1L)
                .userName("testuser1")
                .firstName("John")
                .lastName("Doe")
                .userEmail("john.doe@example.com")
                .build();

        UserProfile userProfile2 = UserProfile.builder()
                .userProfileID(2L)
                .userName("testuser2")
                .firstName("Jane")
                .lastName("Doe")
                .userEmail("jane.doe@example.com")
                .build();

        // Assert
        assertNotEquals(userProfile1, userProfile2);
        assertNotEquals(userProfile1.hashCode(), userProfile2.hashCode());
    }
} 