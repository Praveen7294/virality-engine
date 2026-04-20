package com.praveen.guardrail.virality_engine.dto;

import com.praveen.guardrail.virality_engine.entity.AuthorType;

import java.time.LocalDateTime;

public class CommentResponseDTO {

    private final Long id;

    private final Long postId;

    private final Long authorId;

    private final AuthorType authorType;

    private final String content;

    private final Integer depthLevel;

    private final Long parentCommentId;

    private final LocalDateTime createdAt;

    public CommentResponseDTO(
            final Long id,
            final Long postId,
            final Long authorId,
            final AuthorType authorType,
            final String content,
            final Integer depthLevel,
            final Long parentCommentId,
            final LocalDateTime createdAt) {
        this.id = id;
        this.postId = postId;
        this.authorId = authorId;
        this.authorType = authorType;
        this.content = content;
        this.depthLevel = depthLevel;
        this.parentCommentId = parentCommentId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public AuthorType getAuthorType() {
        return authorType;
    }

    public String getContent() {
        return content;
    }

    public Integer getDepthLevel() {
        return depthLevel;
    }

    public Long getParentCommentId() {
        return parentCommentId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
