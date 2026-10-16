package com.quickbite.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quickbite.dto.request.ChangePasswordRequestDto;
import com.quickbite.dto.request.ForgotPasswordRequestDto;
import com.quickbite.dto.request.LoginRequestDto;
import com.quickbite.dto.request.RegisterRequestDto;
import com.quickbite.dto.request.ResetPasswordRequestDto;
import com.quickbite.dto.response.AuthResponseDto;
import com.quickbite.dto.response.MessageResponseDto;
import com.quickbite.dto.response.UserProfileResponseDto;
import com.quickbite.service.IUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController 
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final IUserService userService;

	public AuthController(IUserService userService) {
		this.userService = userService; 
	} 
		
	
	// Register
	@PostMapping("/register")
	@Operation(summary = "Register User", description = "Customer, Delivery partner and Restaurant owner can able to register to the application")
	public ResponseEntity<AuthResponseDto> register(@RequestBody RegisterRequestDto registerDto) {
		
		return ResponseEntity.ok(userService.register(registerDto)); 
	} 
		
	
	// Login
	@PostMapping("/login")  
	@Operation(summary = "Login User", description = "Customer, Delivery partner and Restaurant owner can able to login to the application")
	public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto loginDto) {
		
		return ResponseEntity.ok(userService.login(loginDto)); 
	}
	
	
	// Get Current User
	@GetMapping("/me")
	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "Get current User", description = "Return the account information for currently authentocated user")
	public ResponseEntity<UserProfileResponseDto> getCurrentUser(Authentication authentication) {
		
		String email = authentication.getName();
		
		return ResponseEntity.ok(userService.getCurrentUser(email));  
	}
	
		
	// Change Password
	@PatchMapping("/change-password")
	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "Change Password", description = "Authenticated User can change their password")
	public ResponseEntity<MessageResponseDto> changePassword(@RequestBody ChangePasswordRequestDto request, Authentication authentication) {
		
		String email = authentication.getName();
		
	    System.out.println("CHANGE PASSWORD CONTROLLER CALLED");
	    System.out.println("Authenticated user: " + authentication.getName());
		
		return ResponseEntity.ok(userService.changePassword(email, request)); 
	}
		
	
	// Forgot Password 
	@PostMapping("/forgot-password")
	@Operation(summary = "Forgot Password", description = "UnAuthentic User can forgot their password by generting reset token and send to email")
	public ResponseEntity<MessageResponseDto> forgotPassword(@RequestBody ForgotPasswordRequestDto request) {
		
		return ResponseEntity.ok(userService.forgotPassword(request));  
	}
	
	
	// Reset Password
	@PostMapping("/reset-password")
	@Operation(summary = "Reset Password", description = "UnAuthentic User can reset their password by reset token")
	public ResponseEntity<MessageResponseDto> resetPassword(@RequestBody ResetPasswordRequestDto request) {
		
		return ResponseEntity.ok(userService.resetPassword(request)); 
	}
	
	
	// Logout
	@PostMapping("/logout")
	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "Logout User", description = "Logs out the currently authenticated user")
	public ResponseEntity<MessageResponseDto> logout() {
		
		return ResponseEntity.ok(userService.logout()); 
	}
		
}