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
 * Permission entity for fine-grained authorization.
 * Represents a specific permission that can be assigned to roles.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("t_permission")
public class Permission {

    @Id
    @Column("permission_id")
    private Long permissionId;

    @Column("permission_name")
    private String permissionName;

    @Column("permission_code")
    private String permissionCode;

    @Column("resource")
    private String resource;

    @Column("action")
    private String action;

    @Column("description")
    private String description;

    @Column("created_at")
    private LocalDateTime createdAt;
}
