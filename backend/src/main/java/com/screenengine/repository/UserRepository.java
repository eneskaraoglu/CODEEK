package com.screenengine.repository;

import com.screenengine.model.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for User entity.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Find user by username
     */
    @Query("SELECT * FROM t_user WHERE username = :username")
    Optional<User> findByUsername(@Param("username") String username);

    /**
     * Find user by email
     */
    @Query("SELECT * FROM t_user WHERE email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    /**
     * Check if username exists
     */
    @Query("SELECT COUNT(*) > 0 FROM t_user WHERE username = :username")
    boolean existsByUsername(@Param("username") String username);

    /**
     * Check if email exists
     */
    @Query("SELECT COUNT(*) > 0 FROM t_user WHERE email = :email")
    boolean existsByEmail(@Param("email") String email);
}
