package com.ucms.backend.exception;

public class InvalidRoleUpdateException extends RuntimeException {
    public InvalidRoleUpdateException(String message) {
        super(message);
    }
}
