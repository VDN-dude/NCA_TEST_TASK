package com.nca.exception;

/**
 * {@code CommentsNotBelongToNewsException} to throw exception
 * when user tries to get access to the comment which not belong to provided news.
 */

public class CommentsNotBelongToNewsException extends RuntimeException {

    public CommentsNotBelongToNewsException(String message) {
        super(message);
    }
}
