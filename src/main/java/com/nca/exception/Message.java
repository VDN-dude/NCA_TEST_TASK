package com.nca.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Message {

    COMMENTS_NOT_BELONG_TO_NEWS("Comment with id: %d, is not belong to the News with id: %d"),
    ENTITY_NOT_FOUND("%s with provided credentials not found.");

    private final String message;
}
