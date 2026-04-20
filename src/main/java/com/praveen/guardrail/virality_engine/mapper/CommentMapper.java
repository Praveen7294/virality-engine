package com.praveen.guardrail.virality_engine.mapper;

import com.praveen.guardrail.virality_engine.dto.CommentRequestDTO;
import com.praveen.guardrail.virality_engine.dto.CommentResponseDTO;
import com.praveen.guardrail.virality_engine.entity.Comment;

public class CommentMapper {

    public static CommentResponseDTO toDTO(Comment comment) {

        Long parentCommentId = null;
        if (comment.getParentComment() != null) {
            parentCommentId = comment.getParentComment().getId();
        }

        return new CommentResponseDTO(
                comment.getId(),
                comment.getPostId(),
                comment.getAuthorId(),
                comment.getAuthorType(),
                comment.getContent(),
                comment.getDepthLevel(),
                parentCommentId,
                comment.getCreatedAt()

        );
    }

    public static Comment toEntity(
            Long postId,
            Integer depthLevel,
            Comment parentCommentId,
            CommentRequestDTO commentRequestDTO) {
        return new Comment(
                postId,
                commentRequestDTO.getAuthorId(),
                commentRequestDTO.getAuthorType(),
                commentRequestDTO.getContent(),
                depthLevel,
                parentCommentId
        );
    }
}
