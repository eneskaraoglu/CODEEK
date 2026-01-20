package com.screenengine.service;

import com.screenengine.config.ScreenEngineProperties;
import com.screenengine.dto.AuthResponse;
import com.screenengine.dto.LoginRequest;
import com.screenengine.dto.RegisterRequest;
import com.screenengine.model.Role;
import com.screenengine.model.User;
import com.screenengine.repository.RoleRepository;
import com.screenengine.repository.UserRepository;
import com.screenengine.security.JwtTokenProvider;
import com.screenengine.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for authentication and user management.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final ScreenEngineProperties properties;
    private final JdbcTemplate jdbcTemplate;

    /**
     * Authenticate user and generate JWT token
     */
    @Transactional
    public AuthResponse login(LoginRequest loginRequest) {
        log.info("Login attempt for username: {}", loginRequest.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        // Update last login
        jdbcTemplate.update(
                "UPDATE t_user SET last_login = ?, failed_attempts = 0 WHERE user_id = ?",
                LocalDateTime.now(), userPrincipal.getUserId()
        );

        String jwt = tokenProvider.generateToken(authentication);

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        log.info("User {} logged in successfully", loginRequest.getUsername());

        return AuthResponse.builder()
                .token(jwt)
                .tokenType("Bearer")
                .expiresIn(properties.getSecurity().getJwt().getExpiration())
                .user(AuthResponse.UserInfo.builder()
                        .userId(userPrincipal.getUserId())
                        .username(userPrincipal.getUsername())
                        .email(userPrincipal.getEmail())
                        .fullName(userPrincipal.getFullName())
                        .fabrikaKod(userPrincipal.getFabrikaKod())
                        .roles(roles)
                        .build())
                .build();
    }

    /**
     * Register new user
     */
    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        log.info("Registration attempt for username: {}", registerRequest.getUsername());

        // Validate username
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }

        // Validate email
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email is already registered");
        }

        // Create new user
        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .email(registerRequest.getEmail())
                .fullName(registerRequest.getFullName())
                .phone(registerRequest.getPhone())
                .fabrikaKod(registerRequest.getFabrikaKod() != null ? registerRequest.getFabrikaKod() : 101L)
                .active(1)
                .locked(0)
                .passwordExpired(0)
                .failedAttempts(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .createdBy("SYSTEM")
                .build();

        User savedUser = userRepository.save(user);

        // Assign default role (ROLE_USER)
        Role userRole = roleRepository.findByRoleCode("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        jdbcTemplate.update(
                "INSERT INTO t_user_role (user_id, role_id, created_by) VALUES (?, ?, ?)",
                savedUser.getUserId(), userRole.getRoleId(), "SYSTEM"
        );

        log.info("User {} registered successfully", registerRequest.getUsername());

        // Auto-login after registration
        LoginRequest loginRequest = new LoginRequest(
                registerRequest.getUsername(),
                registerRequest.getPassword()
        );

        return login(loginRequest);
    }
}
