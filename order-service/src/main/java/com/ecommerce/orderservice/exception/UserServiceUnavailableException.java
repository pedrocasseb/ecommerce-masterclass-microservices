package com.ecommerce.orderservice.exception;

public class UserServiceUnavailableException extends RuntimeException {

    public UserServiceUnavailableException(String userId, Throwable cause) {
        super("User service unavailable while fetching user: " + userId, cause);
    }
}