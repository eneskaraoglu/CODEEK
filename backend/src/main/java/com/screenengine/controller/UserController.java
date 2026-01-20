package com.screenengine.controller;

import com.screenengine.dto.ApiResponse;
import com.screenengine.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * User management controller with role-based authorization examples.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

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
     * Admin-only endpoint
     * Only accessible by users with ROLE_ADMIN or ROLE_SUPER_ADMIN
     */
    @GetMapping("/admin-only")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ApiResponse<String> adminOnlyEndpoint() {
        return ApiResponse.success("This is an admin-only endpoint", "Access granted");
    }

    /**
     * Manager or higher endpoint
     * Accessible by ROLE_MANAGER, ROLE_ADMIN, ROLE_SUPER_ADMIN
     */
    @GetMapping("/manager-or-higher")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'SUPER_ADMIN')")
    public ApiResponse<String> managerOrHigherEndpoint() {
        return ApiResponse.success("This endpoint requires manager role or higher", "Access granted");
    }

    /**
     * User or higher endpoint
     * Accessible by any authenticated user with a role
     */
    @GetMapping("/user-access")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN', 'SUPER_ADMIN')")
    public ApiResponse<String> userAccessEndpoint() {
        return ApiResponse.success("This endpoint is accessible by any authenticated user", "Access granted");
    }

    /**
     * Super admin only endpoint
     * Only accessible by ROLE_SUPER_ADMIN
     */
    @GetMapping("/super-admin-only")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ApiResponse<String> superAdminOnlyEndpoint() {
        return ApiResponse.success("This is a super admin-only endpoint", "Access granted");
    }

    /**
     * Example: Create user (Admin only)
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ApiResponse<String> createUser(@RequestBody Map<String, Object> userData,
                                          @AuthenticationPrincipal UserPrincipal currentUser) {
        log.info("User {} is creating a new user", currentUser.getUsername());
        return ApiResponse.success("User created successfully (mock)", "Success");
    }

    /**
     * Example: Update user (Admin or Manager)
     */
    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'SUPER_ADMIN')")
    public ApiResponse<String> updateUser(@PathVariable Long userId,
                                          @RequestBody Map<String, Object> userData,
                                          @AuthenticationPrincipal UserPrincipal currentUser) {
        log.info("User {} is updating user {}", currentUser.getUsername(), userId);
        return ApiResponse.success("User updated successfully (mock)", "Success");
    }

    /**
     * Example: Delete user (Admin only)
     */
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ApiResponse<String> deleteUser(@PathVariable Long userId,
                                          @AuthenticationPrincipal UserPrincipal currentUser) {
        log.info("User {} is deleting user {}", currentUser.getUsername(), userId);
        return ApiResponse.success("User deleted successfully (mock)", "Success");
    }

    /**
     * Example: View users (Any authenticated user)
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<String> listUsers(@AuthenticationPrincipal UserPrincipal currentUser) {
        log.info("User {} is viewing user list", currentUser.getUsername());
        return ApiResponse.success("User list (mock)", "Success");
    }
}
