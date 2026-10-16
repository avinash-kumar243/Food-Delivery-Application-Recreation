package com.quickbite.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.quickbite.dto.response.ErrorResponseDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

	
    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleRestaurantNotFound(RestaurantNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    
    @ExceptionHandler(UnauthorizedRestaurantAccessException.class)
    public ResponseEntity<ErrorResponseDto> handleUnauthorizedAccess(UnauthorizedRestaurantAccessException ex) {
        return buildError(HttpStatus.FORBIDDEN, ex.getMessage());
    }
 
    
    @ExceptionHandler(InvalidRestaurantOperationException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidOperation(InvalidRestaurantOperationException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
			               .getFieldErrors()
			               .stream()
			               .map(error -> error.getField() + ": " + error.getDefaultMessage())
			               .collect(Collectors.joining(", "));

        return buildError(HttpStatus.BAD_REQUEST, message);
    }
    

    private ResponseEntity<ErrorResponseDto> buildError(HttpStatus status, String message) {

        ErrorResponseDto response = ErrorResponseDto.builder()
									                .status(status.value())
									                .message(message)
									                .timestamp(LocalDateTime.now())
									                .build();

        return ResponseEntity.status(status).body(response);
    }
}