package com.praveen.guardrail.virality_engine.exception;

import com.praveen.guardrail.virality_engine.dto.ErrorResponseDTO;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handlePostNotFoundException(PostNotFoundException ex) {

        logger.error("Post not found: {}", ex.getMessage());

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                "POST_NOT_FOUND",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDTO);
    }

    @ExceptionHandler(CommentDepthLimitExceededException.class)
    public ResponseEntity<ErrorResponseDTO> handleCommentDepthLimitExceededException(
            CommentDepthLimitExceededException ex) {

        logger.error("Comment depth limit exceeded: {}", ex.getMessage());

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                "COMMENT_DEPTH_LIMIT_EXCEEDED",
                ex.getMessage(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponseDTO);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleCommentNotFoundException(CommentNotFoundException ex) {

        logger.error("Comment not found: {}", ex.getMessage());

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                "COMMENT_NOT_FOUND",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDTO);
    }

    @ExceptionHandler(CommentPostMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleCommentPostMismatchException(CommentPostMismatchException ex) {

        logger.error("Comment post mismatch: {}", ex.getMessage());

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                "COMMENT_POST_MISMATCH",
                ex.getMessage(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponseDTO);
    }

    @ExceptionHandler(TooManyBotRepliesException.class)
    public ResponseEntity<ErrorResponseDTO> handleTooManyBotRepliesException(TooManyBotRepliesException ex) {

        logger.error("Too many bot replies: {}", ex.getMessage());

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                "TOO_MANY_REPLIES",
                ex.getMessage(),
                HttpStatus.TOO_MANY_REQUESTS.value(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(errorResponseDTO);
    }

    @ExceptionHandler(BotCoolDownException.class)
    public ResponseEntity<ErrorResponseDTO> handleBotCoolDownException(BotCoolDownException ex) {

        logger.error("Bot cool down: {}", ex.getMessage());

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                "COOLDOWN_ACTIVE",
                ex.getMessage(),
                HttpStatus.TOO_MANY_REQUESTS.value(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(errorResponseDTO);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {

        logger.error("Unexpected error: {}", ex.getMessage());

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                "INTERNAL_ERROR",
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDTO);
    }
}
