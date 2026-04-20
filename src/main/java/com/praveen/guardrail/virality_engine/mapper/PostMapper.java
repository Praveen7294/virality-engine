package com.praveen.guardrail.virality_engine.mapper;

import com.praveen.guardrail.virality_engine.dto.PostRequestDTO;
import com.praveen.guardrail.virality_engine.dto.PostResponseDTO;
import com.praveen.guardrail.virality_engine.entity.Post;

public class PostMapper {

    public static PostResponseDTO toDTO(Post post) {

        return new PostResponseDTO(
                post.getId(),
                post.getAuthorId(),
                post.getAuthorType(),
                post.getContent(),
                post.getCreatedAt()
        );
    }

    public static Post toEntity(PostRequestDTO postRequestDTO) {
        return new Post(
                postRequestDTO.getAuthorId(),
                postRequestDTO.getAuthorType(),
                postRequestDTO.getContent()
        );
    }
}
