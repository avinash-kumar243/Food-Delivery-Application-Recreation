package com.quickbite.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.quickbite.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

// This class is responsible for all the JWT related operations
@Component
public class JwtUtil {
	
	@Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    
    private SecretKey getSigningKey() { 
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); 
    }
    
    
    // Generate Token
    public String generateToken(User user) {
    		return Jwts.builder()
    				   .subject(user.getEmail())
    				   .claim("userId", user.getUserId())
    				   .claim("role", user.getRole())
    				   .issuedAt(new Date())
    				   .expiration(new Date(System.currentTimeMillis() + expiration))
    				   .signWith(getSigningKey())
    				   .compact(); 
    }
    
    
    // Extract email from token
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }
    
    
    // Extract role from token
    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    
    // Extract user id from token
    public Long extractUserId(String token) {
        Number userId = extractClaims(token).get("userId", Number.class);
        return userId.longValue();
    }

    
    // Check if token is valid or not
    public boolean isTokenValid(String token, String email) {

        String tokenEmail = extractEmail(token);

        return tokenEmail.equals(email)
                && !isTokenExpired(token);
    }

    
    // Check if token is expired or not
    private boolean isTokenExpired(String token) {
        return extractClaims(token)
                .getExpiration()
                .before(new Date());
    }
 
    
    // Private helper method for extract claims
    private Claims extractClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload(); 
    }
    
}