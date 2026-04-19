package com.praveen.guardrail.virality_engine.repository;

import com.praveen.guardrail.virality_engine.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
