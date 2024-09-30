package com.nca.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Message {

    ENTITY_ALREADY_EXISTS("%s already exists with provided %s"),
    COMMENTS_NOT_BELONG_TO_NEWS("Comment with id: %d, is not belong to the News with id: %d."),
    BAD_PARAMETERS_VALUE("Bad parameters value: %s"),
    UNKNOWN_PROPERTY("Unknown property: '%s'. The body can only have only these properties: %s."),
    AUTHENTICATION_FAILED("Authentication failed."),
    CHANGE_RESOURCE_ACCESS_DENIED("You have NOT permission to change this %s with id: %d."),
    ENTITY_NOT_FOUND("%s with provided credentials not found.");

    private final String message;
}
