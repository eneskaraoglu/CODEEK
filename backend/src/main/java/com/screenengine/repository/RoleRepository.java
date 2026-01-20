package com.screenengine.repository;

import com.screenengine.model.Role;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Role entity.
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

    /**
     * Find role by role code
     */
    @Query("SELECT * FROM t_role WHERE role_code = :roleCode")
    Optional<Role> findByRoleCode(@Param("roleCode") String roleCode);

    /**
     * Find roles by user ID
     */
    @Query("""
        SELECT r.* FROM t_role r
        INNER JOIN t_user_role ur ON r.role_id = ur.role_id
        WHERE ur.user_id = :userId AND r.active = 1
        """)
    List<Role> findByUserId(@Param("userId") Long userId);

    /**
     * Find all active roles
     */
    @Query("SELECT * FROM t_role WHERE active = 1 ORDER BY role_name")
    List<Role> findAllActive();
}
