package com.quickbite.dto.request;

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
public class ChangePasswordRequestDto {
	
	private String oldPassword;
	private String newPassword;
	private String confirmPassword;
}