package com.quickbite.dto.response;

import java.time.LocalDateTime;

import com.quickbite.model.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponseDto {

	private Long userId;
    private String username;
    private String email;
    private String phone;
    private Role role; 
    private boolean active;
    private LocalDateTime createdAt; 
}