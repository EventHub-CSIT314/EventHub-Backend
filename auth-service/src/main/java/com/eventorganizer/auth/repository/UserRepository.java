package com.eventorganizer.auth.repository;

import com.eventorganizer.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);
    Optional<User> findByUserEmail(String email);
    boolean existsByUserName(String userName);
    boolean existsByUserEmail(String email);
} 