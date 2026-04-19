package com.praveen.guardrail.virality_engine.repository;

import com.praveen.guardrail.virality_engine.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
