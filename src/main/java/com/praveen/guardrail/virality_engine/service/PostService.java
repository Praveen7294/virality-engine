package com.praveen.guardrail.virality_engine.service;

import com.praveen.guardrail.virality_engine.entity.Post;

public interface PostService {

    Post createPost(Long authorId, String authorType, String content);
}
