package com.praveen.guardrail.virality_engine.dto;

import com.praveen.guardrail.virality_engine.entity.AuthorType;
import jakarta.validation.constraints.NotBlank;

public class PostRequestDTO {

    @NotBlank(message = "Author ID is required")
    private Long authorId;

    @NotBlank(message = "Author Type is required")
    private AuthorType authorType;

    private String content;

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public AuthorType getAuthorType() {
        return authorType;
    }

    public void setAuthorType(AuthorType authorType) {
        this.authorType = authorType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
