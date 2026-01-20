package com.screenengine.security;

import com.screenengine.model.Role;
import com.screenengine.model.User;
import com.screenengine.repository.RoleRepository;
import com.screenengine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Custom UserDetailsService implementation for loading user-specific data.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by username: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Load user roles
        List<Role> roles = roleRepository.findByUserId(user.getUserId());
        Set<Role> roleSet = new HashSet<>(roles);
        user.setRoles(roleSet);

        log.debug("User loaded: {}, roles: {}", username, roles.size());

        return UserPrincipal.create(user, roleSet);
    }

    /**
     * Load user by user ID
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long userId) {
        log.debug("Loading user by ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

        // Load user roles
        List<Role> roles = roleRepository.findByUserId(user.getUserId());
        Set<Role> roleSet = new HashSet<>(roles);
        user.setRoles(roleSet);

        return UserPrincipal.create(user, roleSet);
    }
}
