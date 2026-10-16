package com.quickbite.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.quickbite.dto.request.ChangePasswordRequestDto;
import com.quickbite.dto.request.ForgotPasswordRequestDto;
import com.quickbite.dto.request.LoginRequestDto;
import com.quickbite.dto.request.RegisterRequestDto;
import com.quickbite.dto.request.ResetPasswordRequestDto;
import com.quickbite.dto.response.AuthResponseDto;
import com.quickbite.dto.response.MessageResponseDto;
import com.quickbite.dto.response.UserProfileResponseDto;
import com.quickbite.exception.InvalidCredentialsException;
import com.quickbite.exception.InvalidRoleException;
import com.quickbite.exception.UserAlreadyExistsException;
import com.quickbite.exception.UsernameNotFoundException;
import com.quickbite.model.PasswordResetToken;
import com.quickbite.model.Role;
import com.quickbite.model.User;
import com.quickbite.repository.PasswordResetTokenRepository;
import com.quickbite.repository.UserRepository;
import com.quickbite.security.JwtUtil;
import com.quickbite.service.IUserService;

@Service
public class UserService implements IUserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final PasswordResetTokenRepository passwordResetTokenRepository;
	private final JwtUtil jwtUtil;

	// Constructor Injection
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, PasswordResetTokenRepository passwordResetTokenRepository, JwtUtil jwtUtil) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder; 
		this.passwordResetTokenRepository = passwordResetTokenRepository; 
		this.jwtUtil = jwtUtil; 
	}

		
	@Override
	public AuthResponseDto register(RegisterRequestDto request) {
		
		// Check for duplicate email
		if(userRepository.existsByEmail(request.getEmail())) {
			throw new UserAlreadyExistsException("User already found with this email: " + request.getEmail());
		}  
		
		// Check for matching of password and confirm password
		if(request.getPassword() == null || !request.getPassword().equals(request.getConfirmPassword())) {
			throw new InvalidCredentialsException("Password and confirm password does not match");
		}
		
		// Reject ADMIN registration 
		if(request.getRole() == Role.ADMIN) {
			throw new InvalidRoleException("Admin registration is not allowed");
		}
		
		
		User user = User.builder()
						.username(request.getName())
						.email(request.getEmail())
						.password(passwordEncoder.encode(request.getPassword()))
						.phone(request.getPhone())
						.role(request.getRole())
						.active(true)
						.createdAt(LocalDateTime.now())
						.build();  
		
		User savedUser = userRepository.save(user);
		
		String token = jwtUtil.generateToken(savedUser);
		
		return AuthResponseDto.builder()
							  .userId(savedUser.getUserId())
							  .email(savedUser.getEmail())
							  .role(savedUser.getRole())
							  .message("Registration Successful")
							  .token(token)
							  .build();  
	}
 
	@Override 
	public AuthResponseDto login(LoginRequestDto request) {
		
		// Check if user account is present or not
		User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() ->
							new UsernameNotFoundException("User not found with this email: " + request.getEmail())
		);
		 
		
		// Match Password
		if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new InvalidCredentialsException("Wrong Password!!!"); 
		}
		
		
		// Check if user account is active or not
		if(!user.isActive()) {
			throw new InvalidCredentialsException("User account is inactive right now");
		}
		
		
		String token = jwtUtil.generateToken(user); 
		
		return AuthResponseDto.builder()
							  .userId(user.getUserId())
							  .email(user.getEmail())
							  .role(user.getRole()) 
							  .message("Login successfully")
							  .token(token)
							  .build(); 
	}
	
	
	// Get current user details
	public UserProfileResponseDto getCurrentUser(String email) {
		  
		// Check if user account is present or not
		User user = userRepository.findByEmail(email).orElseThrow(() ->
							new UsernameNotFoundException("User not found with this email: " + email)
		);
		
		
		return UserProfileResponseDto.builder()
									 .userId(user.getUserId())
									 .username(user.getUsername())
									 .email(user.getEmail())
									 .phone(user.getPhone())
									 .role(user.getRole())
									 .active(true)
									 .createdAt(user.getCreatedAt())
									 .build(); 
		
	}
	
	
	// Change Password
	public MessageResponseDto changePassword(String email, ChangePasswordRequestDto request) {
		
		// Get currently logged in user
		User user = userRepository.findByEmail(email).orElseThrow(() -> 
							new UsernameNotFoundException("User not found with this email: " + email)); 
		
		// Match passwords
		if(passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
			throw new InvalidCredentialsException("Wrong Old password");
		}
		 
		// Check new password and conform password
		if(request.getNewPassword() == null && request.getNewPassword().equals(request.getConfirmPassword())) {
			throw new InvalidCredentialsException("New password and conform password must be same");
		}
		
		// New Password and Old password must not be same
		if(request.getNewPassword().equals(request.getOldPassword())) {
			throw new InvalidCredentialsException("Old password and new password must not be same"); 
		}
		
		// Encode and update password
		user.setPassword(passwordEncoder.encode(request.getNewPassword())); 
		
		userRepository.save(user);
		
		return new MessageResponseDto("Password changed successfully"); 
	}
	
	
	// Forgot password 
	public MessageResponseDto forgotPassword(ForgotPasswordRequestDto request) {
		
		// Find User
		User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> 
							new UsernameNotFoundException("User not found with this email: " + request.getEmail())); 

		
		// Remove previous password reset token, if any have
		passwordResetTokenRepository.deleteByUser(user);
		
		String resetToken = UUID.randomUUID().toString();
		
		PasswordResetToken passwordResetToken = PasswordResetToken.builder()
																 .token(resetToken) 
																 .user(user)
																 .expiryTime(LocalDateTime.now().plusMinutes(15))
																 .build();
		
		passwordResetTokenRepository.save(passwordResetToken); 
		
		
		// Return success message for temporay, later we will send reset token to email
		return new MessageResponseDto("Password reset token: " + resetToken);   
	}
	
	
	// Reset Password
	public MessageResponseDto resetPassword(ResetPasswordRequestDto request) {
		
		// Get old reset token for that user
		PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.getToken()).orElseThrow(() -> 
									new InvalidCredentialsException("Invalid password reset token"));
		
		// Old reset token expiration
		if(resetToken.getExpiryTime().isBefore(LocalDateTime.now())) {
			
			passwordResetTokenRepository.delete(resetToken);
			
			throw new InvalidCredentialsException("Password reset token has expired");			
		}
		
		
		// New password and conform password should match
		if(request.getNewPassword() == null || !request.getNewPassword().equals(request.getConfirmPassword())) {
			throw new InvalidCredentialsException("New password and conform password do not match");
		}
		
		
		User user = resetToken.getUser();
		
		// New password should not be same as old password
		if(passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
			throw new InvalidCredentialsException("New password cannot be same as old password"); 
		}
		
		
		// Update password
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		
		userRepository.save(user);
		
		// Delete used token (one - time used token)  
		passwordResetTokenRepository.delete(resetToken);
		
		// Return dto
		return new MessageResponseDto("Password reset successfully"); 
	}
	
	
	// Logout
	public MessageResponseDto logout() {
		
		// Right now, I am implementing simple logout feature.
		// Here, the frontend will delete the stored JWT token for the user who wants to logout.
		// In the future, i will add REDIS to remove JWT at the backed side
		
		return new MessageResponseDto("Logout successful"); 
	}
}