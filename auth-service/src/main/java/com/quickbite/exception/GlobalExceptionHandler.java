package com.quickbite.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUsernameNotFound(UsernameNotFoundException e, HttpServletRequest request) {
		
		return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), request.getRequestURI()); 
	} 
	
	
	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException e, HttpServletRequest request) {
		
		return buildErrorResponse(HttpStatus.CONFLICT, e.getMessage(), request.getRequestURI());
	}
	
	
	@ExceptionHandler(InvalidRoleException.class)
	public ResponseEntity<ErrorResponse> handleInvalidRoleException(InvalidRoleException e, HttpServletRequest request) {
		
		return buildErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), request.getRequestURI());
	}
	
	
	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException e, HttpServletRequest request) {
		
		return buildErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), request.getRequestURI()); 
	}
	
	
	// Private helper method
	private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message, String path) {
		
		ErrorResponse errorResponse = ErrorResponse.builder()
												   .timeStamp(LocalDateTime.now())
												   .status(status.value())
												   .message(message) 
												   .path(path)
												   .build();
		
		return ResponseEntity.status(status).body(errorResponse); 
	}
}