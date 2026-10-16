package com.quickbite.exception;

public class UnauthorizedRestaurantAccessException extends RuntimeException {
    public UnauthorizedRestaurantAccessException(String message) {
        super(message);
    }
}