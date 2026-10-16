package com.quickbite.dto.request;

import com.quickbite.model.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {

	private String name;
	private String email;
	private String password;
	private String confirmPassword; 
	private String phone;
	private Role role;
	private boolean active;
} 