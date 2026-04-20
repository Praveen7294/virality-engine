package com.praveen.guardrail.virality_engine.service;

import com.praveen.guardrail.virality_engine.dto.PostRequestDTO;
import com.praveen.guardrail.virality_engine.dto.PostResponseDTO;
import com.praveen.guardrail.virality_engine.entity.Post;
import com.praveen.guardrail.virality_engine.mapper.PostMapper;
import com.praveen.guardrail.virality_engine.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public PostResponseDTO createPost(PostRequestDTO postRequestDTO) {

        Post post = PostMapper.toEntity(postRequestDTO);

        Post savedPost = postRepository.save(post);

        return PostMapper.toDTO(savedPost);
    }
}
