package com.quickbite.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.quickbite.model.Role;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@JsonPropertyOrder({  // The auth response will come in this fixed order only on postman or swagger
    "userId",
    "email",
    "role",
    "message",
    "token"
})
public class AuthResponseDto {

	private Long userId;
    private String email;
    private Role role;
    private String message;
    private String token;
} 