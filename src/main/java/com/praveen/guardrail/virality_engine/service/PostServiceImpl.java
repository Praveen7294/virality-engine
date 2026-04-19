package com.praveen.guardrail.virality_engine.service;

import com.praveen.guardrail.virality_engine.entity.AuthorType;
import com.praveen.guardrail.virality_engine.entity.Post;
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

        Post post = new Post(
                authorId,
                AuthorType.valueOf(authorType.toUpperCase()),
                content
        );

        return postRepository.save(post);
    }
}
