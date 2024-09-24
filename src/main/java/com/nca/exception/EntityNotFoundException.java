package com.nca.exception;

/**
 * {@code EntityNotFoundException} to throw exception when entity not found with provided credentials.
 */

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }
}
