package com.praveen.guardrail.virality_engine.dto;

import com.praveen.guardrail.virality_engine.entity.AuthorType;

import java.time.LocalDateTime;

public class PostResponseDTO {

    private final Long id;

    private final Long author_id;

    private final AuthorType authorType;

    private final String content;

    private final LocalDateTime createdAt;

    public PostResponseDTO(
            final Long id,
            final Long author_id,
            final  AuthorType authorType,
            final String content,
            final LocalDateTime createdAt) {
        this.id = id;
        this.author_id = author_id;
        this.authorType = authorType;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getAuthor_id() {
        return author_id;
    }

    public AuthorType getAuthorType() {
        return authorType;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
