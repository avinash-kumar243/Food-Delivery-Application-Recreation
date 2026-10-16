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
    

    private Claims extractClaims(String token) {

        return Jwts.parser() 
                	.verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    
    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

   
    public Long extractUserId(String token) {

        Object userId = extractClaims(token).get("userId");

        if(!(userId instanceof Number)) {
            throw new IllegalArgumentException("Invalid user ID in JWT");
        }
        return ((Number) userId).longValue();
    }

    
    public boolean isTokenValid(String token) {

        try {
            Claims claims = extractClaims(token);

            Object userId = claims.get("userId");
            String role = claims.get("role", String.class);
            Date expiration = claims.getExpiration();

            return claims.getSubject() != null
                    && !claims.getSubject().isBlank()
                    && userId instanceof Number
                    && role != null
                    && !role.isBlank()
                    && expiration != null
                    && expiration.after(new Date());

        } catch(Exception e) {
            return false;
        } 
    }
}
