package com.quickbite.service;

import com.quickbite.dto.request.ChangePasswordRequestDto;
import com.quickbite.dto.request.ForgotPasswordRequestDto;
import com.quickbite.dto.request.LoginRequestDto;
import com.quickbite.dto.request.RegisterRequestDto;
import com.quickbite.dto.request.ResetPasswordRequestDto;
import com.quickbite.dto.response.AuthResponseDto;
import com.quickbite.dto.response.MessageResponseDto;
import com.quickbite.dto.response.UserProfileResponseDto;

public interface IUserService {
	
	AuthResponseDto register(RegisterRequestDto request);
	
	AuthResponseDto login(LoginRequestDto request);
	
	UserProfileResponseDto getCurrentUser(String email);
	
	MessageResponseDto changePassword(String email, ChangePasswordRequestDto request);
	
	MessageResponseDto forgotPassword(ForgotPasswordRequestDto request); 
	 
	MessageResponseDto resetPassword(ResetPasswordRequestDto request);
	
	MessageResponseDto logout();
}