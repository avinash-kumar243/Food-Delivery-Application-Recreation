package com.quickbite.exception;

public class InvalidRestaurantOperationException extends RuntimeException {
    public InvalidRestaurantOperationException(String message) {
        super(message);
    }
}