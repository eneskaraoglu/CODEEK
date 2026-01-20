package com.screenengine.security;

import com.screenengine.config.ScreenEngineProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * JWT Token Provider for generating and validating JWT tokens.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final ScreenEngineProperties properties;
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        String secret = properties.getSecurity().getJwt().getSecret();
        // Ensure secret is at least 256 bits (32 bytes) for HS256
        if (secret.length() < 32) {
            secret = secret + "0".repeat(32 - secret.length());
        }
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generate JWT token from authentication
     */
    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return generateTokenFromUsername(
            userPrincipal.getUsername(),
            userPrincipal.getUserId(),
            userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())
        );
    }

    /**
     * Generate JWT token from username
     */
    public String generateTokenFromUsername(String username, Long userId, java.util.List<String> roles) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + properties.getSecurity().getJwt().getExpiration());

        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("roles", roles)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Get username from JWT token
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    /**
     * Get user ID from JWT token
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("userId", Long.class);
    }

    /**
     * Validate JWT token
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (SecurityException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty");
        }
        return false;
    }

    /**
     * Get expiration date from token
     */
    public Date getExpirationFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getExpiration();
    }

    /**
     * Check if token is expired
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = getExpirationFromToken(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}
