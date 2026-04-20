package com.praveen.guardrail.virality_engine.dto;

import com.praveen.guardrail.virality_engine.entity.AuthorType;
import jakarta.validation.constraints.NotNull;

public class PostRequestDTO {

    @NotNull(message = "Author ID is required")
    private Long authorId;

    @NotNull(message = "Author Type is required")
    private AuthorType authorType;

    private String content;

    public @NotNull(message = "Author ID is required") Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(@NotNull(message = "Author ID is required") Long authorId) {
        this.authorId = authorId;
    }

    public @NotNull(message = "Author Type is required") AuthorType getAuthorType() {
        return authorType;
    }

    public void setAuthorType(@NotNull(message = "Author Type is required") AuthorType authorType) {
        this.authorType = authorType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
