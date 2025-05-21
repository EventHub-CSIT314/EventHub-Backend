package com.eventorganizer.user.service;

import com.eventorganizer.user.model.UserProfile;
import com.eventorganizer.user.model.UserProfileEvent;
import com.eventorganizer.user.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final KafkaTemplate<String, UserProfileEvent> kafkaTemplate;
    private static final String TOPIC = "user-profile-events";

    @Autowired
    public UserProfileService(UserProfileRepository userProfileRepository, KafkaTemplate<String, UserProfileEvent> kafkaTemplate) {
        this.userProfileRepository = userProfileRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional(readOnly = true)
    public UserProfile getUserProfile(String userName) {
        return userProfileRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("User profile not found for userName: " + userName));
    }

    @Transactional
    public UserProfile updateUserProfile(String username, UserProfile updatedProfile) {
        UserProfile existingProfile = getUserProfile(username);
        
        // Update only non-null fields
        if (updatedProfile.getFirstName() != null) {
            existingProfile.setFirstName(updatedProfile.getFirstName());
        }
        if (updatedProfile.getLastName() != null) {
            existingProfile.setLastName(updatedProfile.getLastName());
        }
        if (updatedProfile.getUserEmail() != null && !updatedProfile.getUserEmail().equals(existingProfile.getUserEmail())) {
            if (userProfileRepository.existsByUserEmail(updatedProfile.getUserEmail())) {
                throw new RuntimeException("Email already in use");
            }
            existingProfile.setUserEmail(updatedProfile.getUserEmail());
        }
        if (updatedProfile.getUserPhone() != null) {
            existingProfile.setUserPhone(updatedProfile.getUserPhone());
        }
//        if (updatedProfile.getBio() != null) {
//            existingProfile.setBio(updatedProfile.getBio());
//        }
        if (updatedProfile.getProfilePictureURL() != null) {
            existingProfile.setProfilePictureURL(updatedProfile.getProfilePictureURL());
        }

        // Publish event
        UserProfileEvent event = new UserProfileEvent(
                "UPDATED",
                existingProfile.getUserName(),
                existingProfile.getUserEmail(),
                existingProfile.getFirstName(),
                existingProfile.getLastName()
        );
        kafkaTemplate.send(TOPIC, existingProfile.getUserName(), event);

        return userProfileRepository.save(existingProfile);
    }

    @Transactional
    public UserProfile createUserProfile(UserProfile userProfile) {
        if (userProfileRepository.existsByUserName(userProfile.getUserName())) {
            throw new RuntimeException("Username already exists");
        }
        if (userProfileRepository.existsByUserEmail(userProfile.getUserEmail())) {
            throw new RuntimeException("Email already in use");
        }
        return userProfileRepository.save(userProfile);
    }

    @Transactional
    public void deleteUserProfile(String userName) {
        UserProfile userProfile = getUserProfile(userName);
        userProfileRepository.delete(userProfile);
    }
} 