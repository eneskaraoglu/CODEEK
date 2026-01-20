package com.screenengine.controller;

import com.screenengine.dto.ApiResponse;
import com.screenengine.dto.AuthResponse;
import com.screenengine.dto.LoginRequest;
import com.screenengine.dto.RegisterRequest;
import com.screenengine.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication controller for login, registration, and token management.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * User login endpoint
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login request received for username: {}", loginRequest.getUsername());

        try {
            AuthResponse authResponse = authService.login(loginRequest);
            return ResponseEntity.ok(ApiResponse.success(authResponse, "Login successful"));
        } catch (Exception e) {
            log.error("Login failed for username: {}", loginRequest.getUsername(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("AUTHENTICATION_FAILED", "Invalid username or password"));
        }
    }

    /**
     * User registration endpoint
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("Registration request received for username: {}", registerRequest.getUsername());

        try {
            AuthResponse authResponse = authService.register(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(authResponse, "Registration successful"));
        } catch (RuntimeException e) {
            log.error("Registration failed for username: {}", registerRequest.getUsername(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("REGISTRATION_FAILED", e.getMessage()));
        } catch (Exception e) {
            log.error("Registration failed for username: {}", registerRequest.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("REGISTRATION_FAILED", "An error occurred during registration"));
        }
    }
}
