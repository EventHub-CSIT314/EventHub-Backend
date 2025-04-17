package com.eventorganizer.user.service;

import com.eventorganizer.user.model.UserProfile;
import com.eventorganizer.user.repository.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private UserProfileService userProfileService;

    private UserProfile userProfile;
    private static final String TEST_USERNAME = "testuser";

    @BeforeEach
    void setUp() {
        userProfile = UserProfile.builder()
                .userProfileID(1L)
                .userName(TEST_USERNAME)
                .firstName("John")
                .lastName("Doe")
                .userEmail("john.doe@example.com")
                .userPhone("1234567890")
                .build();
    }

    @Test
    void getUserProfile_WhenUserExists_ReturnsUserProfile() {
        // Arrange
        when(userProfileRepository.findByUserName(TEST_USERNAME)).thenReturn(Optional.of(userProfile));

        // Act
        UserProfile found = userProfileService.getUserProfile(TEST_USERNAME);

        // Assert
        assertNotNull(found);
        assertEquals(TEST_USERNAME, found.getUserName());
        assertEquals("John", found.getFirstName());
        assertEquals("Doe", found.getLastName());
        assertEquals("john.doe@example.com", found.getUserEmail());
        verify(userProfileRepository).findByUserName(TEST_USERNAME);
    }

    @Test
    void getUserProfile_WhenUserDoesNotExist_ThrowsException() {
        // Arrange
        when(userProfileRepository.findByUserName(TEST_USERNAME)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userProfileService.getUserProfile(TEST_USERNAME));
        verify(userProfileRepository).findByUserName(TEST_USERNAME);
    }

    @Test
    void updateUserProfile_WhenUserExists_UpdatesProfile() {
        // Arrange
        UserProfile updatedProfile = UserProfile.builder()
                .userName(TEST_USERNAME)
                .firstName("Jane")
                .lastName("Smith")
                .userEmail("jane.smith@example.com")
                .userPhone("0987654321")
                .build();

        when(userProfileRepository.findByUserName(TEST_USERNAME)).thenReturn(Optional.of(userProfile));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(updatedProfile);

        // Act
        UserProfile result = userProfileService.updateUserProfile(TEST_USERNAME, updatedProfile);

        // Assert
        assertNotNull(result);
        assertEquals(TEST_USERNAME, result.getUserName());
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("jane.smith@example.com", result.getUserEmail());
        assertEquals("0987654321", result.getUserPhone());

        verify(userProfileRepository).findByUserName(TEST_USERNAME);
        verify(userProfileRepository).save(any(UserProfile.class));
    }

    @Test
    void updateUserProfile_WhenUserDoesNotExist_ThrowsException() {
        // Arrange
        UserProfile updatedProfile = UserProfile.builder()
                .userName(TEST_USERNAME)
                .firstName("Jane")
                .lastName("Smith")
                .userEmail("jane.smith@example.com")
                .build();

        when(userProfileRepository.findByUserName(TEST_USERNAME)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userProfileService.updateUserProfile(TEST_USERNAME, updatedProfile));
        verify(userProfileRepository).findByUserName(TEST_USERNAME);
        verify(userProfileRepository, never()).save(any(UserProfile.class));
    }

    @Test
    void createUserProfile_WithValidData_Success() {
        // Arrange
        when(userProfileRepository.existsByUserName(TEST_USERNAME)).thenReturn(false);
        when(userProfileRepository.existsByUserEmail(userProfile.getUserEmail())).thenReturn(false);
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(userProfile);

        // Act
        UserProfile result = userProfileService.createUserProfile(userProfile);

        // Assert
        assertNotNull(result);
        assertEquals(TEST_USERNAME, result.getUserName());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john.doe@example.com", result.getUserEmail());
        verify(userProfileRepository).existsByUserName(TEST_USERNAME);
        verify(userProfileRepository).existsByUserEmail(userProfile.getUserEmail());
        verify(userProfileRepository).save(userProfile);
    }

    @Test
    void createUserProfile_WhenUsernameExists_ThrowsException() {
        // Arrange
        when(userProfileRepository.existsByUserName(TEST_USERNAME)).thenReturn(true);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userProfileService.createUserProfile(userProfile));
        verify(userProfileRepository).existsByUserName(TEST_USERNAME);
        verify(userProfileRepository, never()).save(any(UserProfile.class));
    }

    @Test
    void createUserProfile_WhenEmailExists_ThrowsException() {
        // Arrange
        when(userProfileRepository.existsByUserName(TEST_USERNAME)).thenReturn(false);
        when(userProfileRepository.existsByUserEmail(userProfile.getUserEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userProfileService.createUserProfile(userProfile));
        verify(userProfileRepository).existsByUserName(TEST_USERNAME);
        verify(userProfileRepository).existsByUserEmail(userProfile.getUserEmail());
        verify(userProfileRepository, never()).save(any(UserProfile.class));
    }

    @Test
    void deleteUserProfile_WhenUserExists_Success() {
        // Arrange
        when(userProfileRepository.findByUserName(TEST_USERNAME)).thenReturn(Optional.of(userProfile));
        doNothing().when(userProfileRepository).delete(userProfile);

        // Act
        userProfileService.deleteUserProfile(TEST_USERNAME);

        // Assert
        verify(userProfileRepository).findByUserName(TEST_USERNAME);
        verify(userProfileRepository).delete(userProfile);
    }

    @Test
    void deleteUserProfile_WhenUserDoesNotExist_ThrowsException() {
        // Arrange
        when(userProfileRepository.findByUserName(TEST_USERNAME)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userProfileService.deleteUserProfile(TEST_USERNAME));
        verify(userProfileRepository).findByUserName(TEST_USERNAME);
        verify(userProfileRepository, never()).delete(any(UserProfile.class));
    }
} 