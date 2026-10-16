package com.quickbite.security;

// This holds the identity extracted from the verified JWT
public record AuthenticatedUser (
        Long userId,
        String email,
        String role
) {}