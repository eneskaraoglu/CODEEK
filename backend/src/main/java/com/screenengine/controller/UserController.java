package com.screenengine.controller;

import com.screenengine.dto.ApiResponse;
import com.screenengine.dto.UpdateUserRequest;
import com.screenengine.dto.UserDTO;
import com.screenengine.security.UserPrincipal;
import com.screenengine.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller for user management operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Get current authenticated user info
     * Accessible by any authenticated user
     */
    @GetMapping("/me")
    public ApiResponse<Map<String, Object>> getCurrentUser(@AuthenticationPrincipal UserPrincipal currentUser) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", currentUser.getUserId());
        userInfo.put("username", currentUser.getUsername());
        userInfo.put("email", currentUser.getEmail());
        userInfo.put("fullName", currentUser.getFullName());
        userInfo.put("fabrikaKod", currentUser.getFabrikaKod());
        userInfo.put("roles", currentUser.getAuthorities());

        return ApiResponse.success(userInfo);
    }

    /**
     * Get all users - Admin only
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        log.info("Fetching all users");
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users, "Users retrieved successfully"));
    }

    /**
     * Get user by ID - Admin only
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long userId) {
        log.info("Fetching user with ID: {}", userId);
        UserDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success(user, "User retrieved successfully"));
    }

    /**
     * Update user - Admin only
     */
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserRequest request) {
        log.info("Updating user with ID: {}", userId);
        UserDTO updatedUser = userService.updateUser(userId, request);
        return ResponseEntity.ok(ApiResponse.success(updatedUser, "User updated successfully"));
    }

    /**
     * Delete user (soft delete) - Admin only
     */
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userId) {
        log.info("Deleting user with ID: {}", userId);
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success(null, "User deleted successfully"));
    }

    /**
     * Toggle user active status - Admin only
     */
    @PatchMapping("/{userId}/toggle-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDTO>> toggleUserStatus(@PathVariable Long userId) {
        log.info("Toggling status for user with ID: {}", userId);
        UserDTO updatedUser = userService.toggleUserStatus(userId);
        return ResponseEntity.ok(ApiResponse.success(updatedUser, "User status updated successfully"));
    }
}
