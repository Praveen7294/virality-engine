package com.praveen.guardrail.virality_engine.exception;

public class TooManyBotRepliesException extends RuntimeException {

    public TooManyBotRepliesException(String message) {
        super(message);
    }
}
