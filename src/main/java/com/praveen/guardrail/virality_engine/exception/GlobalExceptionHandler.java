package com.praveen.guardrail.virality_engine.exception;

import com.praveen.guardrail.virality_engine.dto.ErrorResponseDTO;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidAuthorTypeException(HttpMessageNotReadableException ex) {

        logger.error("Invalid request body: {}", ex.getMessage());

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                "INVALID_REQUEST",
                ex.getMessage(),
                400,
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(errorResponseDTO);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {

        logger.error("Unexpected error: {}", ex.getMessage());

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                "INTERNAL ERROR",
                ex.getMessage(),
                500,
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(errorResponseDTO);
    }
}
