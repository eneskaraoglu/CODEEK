package com.screenengine.security;

import com.screenengine.model.Role;
import com.screenengine.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * UserDetails implementation for Spring Security.
 * Represents the authenticated user principal.
 */
@Data
@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private Long userId;
    private String username;
    private String email;
    private String fullName;
    private String password;
    private Long fabrikaKod;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean accountNonLocked;

    /**
     * Create UserPrincipal from User entity
     */
    public static UserPrincipal create(User user, Set<Role> roles) {
        Collection<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleCode()))
                .collect(Collectors.toList());

        return new UserPrincipal(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getPassword(),
                user.getFabrikaKod(),
                authorities,
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isAccountNonLocked()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
