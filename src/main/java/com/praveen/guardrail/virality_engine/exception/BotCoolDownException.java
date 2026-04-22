package com.praveen.guardrail.virality_engine.exception;

public class BotCoolDownException extends RuntimeException {

    public BotCoolDownException(String message) {
        super(message);
    }
}
