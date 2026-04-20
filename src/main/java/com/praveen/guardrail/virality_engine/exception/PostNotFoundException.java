package com.praveen.guardrail.virality_engine.exception;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(final String message) {
        super(message);
    }
}
