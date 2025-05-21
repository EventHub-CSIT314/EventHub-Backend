package com.eventorganizer.auth.service;

import com.eventorganizer.auth.dto.AuthResponse;
import com.eventorganizer.auth.dto.LoginRequest;
import com.eventorganizer.auth.dto.SignupRequest;
import com.eventorganizer.auth.model.User;
import com.eventorganizer.auth.repository.UserRepository;
import com.eventorganizer.auth.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByUserName(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByUserEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        var user = User.builder()
                .userName(request.getUsername())
                .userEmail(request.getEmail())
                .userPassword(passwordEncoder.encode(request.getPassword()))
                .userRole(request.getRole())
                .build();

        userRepository.save(user);

        var jwtToken = jwtUtils.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getUserName())
                        .password(user.getUserPassword())
                        .roles(user.getUserRole().name())
                        .build()
        );

//        TEST
        System.out.println("Generated token: " + jwtToken);

        return AuthResponse.builder()
                .token(jwtToken)
                .username(user.getUserName())
                .role(user.getUserRole().name())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        System.out.println("Login attempt for user: " + request.getUsername());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            System.out.println("Authentication successful for user: " + request.getUsername());

            var user = userRepository.findByUserName(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            System.out.println("User found in database: " + user.getUserName());

            var jwtToken = jwtUtils.generateToken(
                    org.springframework.security.core.userdetails.User.builder()
                            .username(user.getUserName())
                            .password(user.getUserPassword())
                            .roles(user.getUserRole().name())
                            .build()
            );
            System.out.println("JWT token generated successfully");

            return AuthResponse.builder()
                    .token(jwtToken)
                    .username(user.getUserName())
                    .role(user.getUserRole().name())
                    .build();
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
} 