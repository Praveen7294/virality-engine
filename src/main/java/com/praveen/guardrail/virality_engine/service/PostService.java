package com.praveen.guardrail.virality_engine.service;

import com.praveen.guardrail.virality_engine.dto.PostRequestDTO;
import com.praveen.guardrail.virality_engine.dto.PostResponseDTO;

public interface PostService {

    PostResponseDTO createPost(PostRequestDTO postRequestDTO);
}
