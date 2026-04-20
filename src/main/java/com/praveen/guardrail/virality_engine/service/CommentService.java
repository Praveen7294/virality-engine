package com.praveen.guardrail.virality_engine.service;

import com.praveen.guardrail.virality_engine.dto.CommentRequestDTO;
import com.praveen.guardrail.virality_engine.dto.CommentResponseDTO;

public interface CommentService {

    CommentResponseDTO addComment(Long postId, CommentRequestDTO commentRequestDTO);
}
