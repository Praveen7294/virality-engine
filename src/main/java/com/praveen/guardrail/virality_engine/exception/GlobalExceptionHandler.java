package com.praveen.guardrail.virality_engine.exception;

import com.praveen.guardrail.virality_engine.dto.ErrorResponseDTO;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InvalidAuthorTypeException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidAuthorTypeException(InvalidAuthorTypeException ex) {

        logger.error("Invalid Author Type: {}", ex.getMessage());

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                "INVALID_AUTHOR_TYPE",
                ex.getMessage(),
                400,
                System.currentTimeMillis()
        );

        return ResponseEntity.badRequest().body(errorResponseDTO);
    }
}
