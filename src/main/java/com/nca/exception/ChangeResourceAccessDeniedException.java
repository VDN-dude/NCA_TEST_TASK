package com.nca.exception;

/**
 * {@code ChangeResourceAccessDeniedException} to throw exception when user cannot have access to resource.
 */

public class ChangeResourceAccessDeniedException extends RuntimeException {

    public ChangeResourceAccessDeniedException(String message) {
        super(message);
    }
}
