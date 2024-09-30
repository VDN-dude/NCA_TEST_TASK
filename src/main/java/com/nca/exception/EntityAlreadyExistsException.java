package com.nca.exception;

/**
 * {@code EntityNotFoundException} to throw exception when entity not found with provided credentials.
 */

public class EntityAlreadyExistsException extends RuntimeException {

    public EntityAlreadyExistsException(String message) {
        super(message);
    }
}
