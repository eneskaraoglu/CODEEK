package com.screenengine.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Role entity for user authorization.
 * Represents a role that can be assigned to users.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("t_role")
public class Role {

    @Id
    @Column("role_id")
    private Long roleId;

    @Column("role_name")
    private String roleName;

    @Column("role_code")
    private String roleCode;

    @Column("description")
    private String description;

    @Column("active")
    private Integer active;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    /**
     * Check if role is active
     */
    public boolean isActive() {
        return active != null && active == 1;
    }
}
