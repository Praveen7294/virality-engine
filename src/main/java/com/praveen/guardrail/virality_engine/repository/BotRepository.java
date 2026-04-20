package com.praveen.guardrail.virality_engine.repository;

import com.praveen.guardrail.virality_engine.entity.Bot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BotRepository extends JpaRepository<Bot, Long> {
}
