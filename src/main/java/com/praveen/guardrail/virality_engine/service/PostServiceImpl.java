package com.praveen.guardrail.virality_engine.service;

import com.praveen.guardrail.virality_engine.entity.AuthorType;
import com.praveen.guardrail.virality_engine.entity.Post;
import com.praveen.guardrail.virality_engine.exception.InvalidAuthorTypeException;
import com.praveen.guardrail.virality_engine.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post createPost(Long authorId, String authorType, String content) {

        AuthorType type;

        try {
            type = AuthorType.valueOf(authorType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidAuthorTypeException(
                    "Invalid Author Type: " + authorType + ". Allowed Author: BOT, USER"
            );
        }

        Post post = new Post(
                authorId,
                type,
                content
        );

        return postRepository.save(post);
    }
}
