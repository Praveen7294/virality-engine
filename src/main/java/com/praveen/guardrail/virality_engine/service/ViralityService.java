package com.praveen.guardrail.virality_engine.service;

import com.praveen.guardrail.virality_engine.entity.AuthorType;
import com.praveen.guardrail.virality_engine.util.InteractionType;
import com.praveen.guardrail.virality_engine.util.ViralityUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ViralityService {

    private final StringRedisTemplate stringRedisTemplate;

    public ViralityService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void handleLike(Long postId, AuthorType authorType) {

        String likeKey = "post:" + postId + ":likes";
        String scoreKey = "post:" + postId + ":virality_score";

        stringRedisTemplate.opsForValue().increment(likeKey);

        int score = ViralityUtil.getScoreIncrement(authorType, InteractionType.LIKE);

        stringRedisTemplate.opsForValue().increment(scoreKey, score);
    }

    public void handleComment(Long postId, AuthorType authorType) {

        String scoreKey = "post:" + postId + ":virality_score";

        int score = ViralityUtil.getScoreIncrement(authorType, InteractionType.COMMENT);
        stringRedisTemplate.opsForValue().increment(scoreKey, score);
    }
}
