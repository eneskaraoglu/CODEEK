package com.screenengine.service;

import com.screenengine.dto.UpdateUserRequest;
import com.screenengine.dto.UserDTO;
import com.screenengine.exception.ResourceNotFoundException;
import com.screenengine.model.Role;
import com.screenengine.model.User;
import com.screenengine.repository.RoleRepository;
import com.screenengine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    /**
     * Get all users with their roles
     */
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        Iterable<User> users = userRepository.findAll();
        return StreamSupport.stream(users.spliterator(), false)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get user by ID
     */
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return convertToDTO(user);
    }

    /**
     * Update user
     */
    @Transactional
    public UserDTO updateUser(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Update basic fields if provided
        if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new IllegalArgumentException("Username already exists");
            }
            user.setUsername(request.getUsername());
        }

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
            user.setEmail(request.getEmail());
        }

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }

        if (request.getFabrikaKod() != null) {
            user.setFabrikaKod(request.getFabrikaKod());
        }

        if (request.getStatus() != null) {
            user.setActive("ACTIVE".equalsIgnoreCase(request.getStatus()) ? 1 : 0);
        }

        user.setUpdatedAt(LocalDateTime.now());
        User updatedUser = userRepository.save(user);

        // Update role if provided
        if (request.getRole() != null) {
            updateUserRole(userId, request.getRole());
        }

        return convertToDTO(updatedUser);
    }

    /**
     * Delete user (soft delete by setting inactive)
     */
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        user.setActive(0);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    /**
     * Toggle user active status
     */
    @Transactional
    public UserDTO toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        user.setActive(user.getActive() == 1 ? 0 : 1);
        user.setUpdatedAt(LocalDateTime.now());
        User updatedUser = userRepository.save(user);

        return convertToDTO(updatedUser);
    }

    /**
     * Update user role
     */
    @Transactional
    public void updateUserRole(Long userId, String roleCode) {
        // First, remove existing roles
        jdbcTemplate.update("DELETE FROM t_user_role WHERE user_id = ?", userId);

        // Find the role
        String fullRoleCode = roleCode.startsWith("ROLE_") ? roleCode : "ROLE_" + roleCode;
        Role role = roleRepository.findByRoleCode(fullRoleCode)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleCode));

        // Insert new role
        jdbcTemplate.update(
            "INSERT INTO t_user_role (user_id, role_id, created_at, created_by) VALUES (?, ?, ?, ?)",
            userId, role.getRoleId(), LocalDateTime.now(), "ADMIN"
        );
    }

    /**
     * Convert User entity to UserDTO
     */
    private UserDTO convertToDTO(User user) {
        // Get user roles
        List<Role> roles = roleRepository.findByUserId(user.getUserId());
        List<String> roleNames = roles.stream()
                .map(Role::getRoleCode)
                .collect(Collectors.toList());

        return UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .fabrikaKod(user.getFabrikaKod())
                .roles(roleNames)
                .status(user.getActive() == 1 ? "ACTIVE" : "INACTIVE")
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
