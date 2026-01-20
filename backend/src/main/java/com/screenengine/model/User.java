package com.screenengine.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * User entity for authentication and authorization.
 * Represents a system user with credentials and roles.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("t_user")
public class User {

    @Id
    @Column("user_id")
    private Long userId;

    @Column("username")
    private String username;

    @Column("password")
    private String password;

    @Column("email")
    private String email;

    @Column("full_name")
    private String fullName;

    @Column("phone")
    private String phone;

    @Column("active")
    private Integer active;

    @Column("locked")
    private Integer locked;

    @Column("password_expired")
    private Integer passwordExpired;

    @Column("last_login")
    private LocalDateTime lastLogin;

    @Column("failed_attempts")
    private Integer failedAttempts;

    @Column("fabrika_kod")
    private Long fabrikaKod;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    @Column("created_by")
    private String createdBy;

    @Column("updated_by")
    private String updatedBy;

    /**
     * User roles (not persisted directly, loaded separately)
     */
    @Transient
    private Set<Role> roles = new HashSet<>();

    /**
     * Check if user account is active
     */
    public boolean isActive() {
        return active != null && active == 1;
    }

    /**
     * Check if user account is locked
     */
    public boolean isLocked() {
        return locked != null && locked == 1;
    }

    /**
     * Check if password is expired
     */
    public boolean isPasswordExpired() {
        return passwordExpired != null && passwordExpired == 1;
    }

    /**
     * Check if user account is non-expired
     */
    public boolean isAccountNonExpired() {
        return true; // Can be extended with expiration logic
    }

    /**
     * Check if credentials are non-expired
     */
    public boolean isCredentialsNonExpired() {
        return !isPasswordExpired();
    }

    /**
     * Check if account is non-locked
     */
    public boolean isAccountNonLocked() {
        return !isLocked();
    }

    /**
     * Check if account is enabled
     */
    public boolean isEnabled() {
        return isActive();
    }
}
