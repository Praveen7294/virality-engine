package com.praveen.guardrail.virality_engine.service;

import com.praveen.guardrail.virality_engine.entity.AuthorType;
import com.praveen.guardrail.virality_engine.exception.PostNotFoundException;
import com.praveen.guardrail.virality_engine.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {

    private final PostRepository postRepository;
    private final ViralityService viralityService;

    public LikeServiceImpl(PostRepository postRepository, ViralityService viralityService) {
        this.postRepository = postRepository;
        this.viralityService = viralityService;
    }

    @Override
    public void likePost(Long postId) {

        if (!postRepository.existsById(postId)) {
            throw new PostNotFoundException("Post not found with this id: " + postId);
        }

        viralityService.handleLike(postId, AuthorType.USER);
    }
}
