package com.praveen.guardrail.virality_engine.controller;

import com.praveen.guardrail.virality_engine.dto.CommentRequestDTO;
import com.praveen.guardrail.virality_engine.dto.CommentResponseDTO;
import com.praveen.guardrail.virality_engine.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponseDTO> addComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequestDTO commentRequestDTO) {

        CommentResponseDTO commentResponseDTO =
                commentService.addComment(postId, commentRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(commentResponseDTO);
    }
}
