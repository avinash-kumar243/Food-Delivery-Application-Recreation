package com.quickbite.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
 
    
    // Extract email
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }
    

    // Extract role
    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }
    

    // Extract user ID
    public Long extractUserId(String token) { 
        Number userId = (Number) extractClaims(token).get("userId");

        return userId.longValue();
    }
    

    // Validate JWT
    public boolean isTokenValid(String token) {

        Claims claims = extractClaims(token);

        return claims.getSubject() != null
                && !claims.getSubject().isBlank()
                && claims.get("userId") instanceof Number
                && claims.get("role") instanceof String
                && claims.getExpiration() != null
                && claims.getExpiration().after(new Date());
    }

    private Claims extractClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
