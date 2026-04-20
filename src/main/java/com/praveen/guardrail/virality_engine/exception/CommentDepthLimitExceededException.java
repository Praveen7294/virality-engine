package com.praveen.guardrail.virality_engine.exception;

public class CommentDepthLimitExceededException extends RuntimeException {

    public CommentDepthLimitExceededException(String message) {
        super(message);
    }
}
