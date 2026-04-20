package com.praveen.guardrail.virality_engine.dto;

public class ErrorResponseDTO {

    private final String error;

    private final String message;

    private final Integer status;

    private final Long timestamp;

    public ErrorResponseDTO(String error, String message, Integer status, Long timestamp) {
        this.error = error;
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
