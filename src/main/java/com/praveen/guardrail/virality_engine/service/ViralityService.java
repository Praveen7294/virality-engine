package com.praveen.guardrail.virality_engine.service;

import com.praveen.guardrail.virality_engine.entity.AuthorType;
import com.praveen.guardrail.virality_engine.entity.Comment;
import com.praveen.guardrail.virality_engine.entity.Post;
import com.praveen.guardrail.virality_engine.exception.BotCoolDownException;
import com.praveen.guardrail.virality_engine.exception.TooManyBotRepliesException;
import com.praveen.guardrail.virality_engine.util.InteractionType;
import com.praveen.guardrail.virality_engine.util.ViralityUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ViralityService {

    private final static int MAX_BOT_REPLIES = 100;
    private final static int BOT_COOLDOWN_TTL = 600;

    private final StringRedisTemplate stringRedisTemplate;

    public ViralityService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    // virality logic
    public void handleLike(Long postId, AuthorType authorType) {

        String likeKey = "post:" + postId + ":likes";
        String scoreKey = "post:" + postId + ":virality_score";

        stringRedisTemplate.opsForValue().increment(likeKey);

        int score = ViralityUtil.getScoreIncrement(authorType, InteractionType.LIKE);

        stringRedisTemplate.opsForValue().increment(scoreKey, score);
    }

    // virality logic
    public void handleComment(Long postId, AuthorType authorType) {

        String scoreKey = "post:" + postId + ":virality_score";

        int score = ViralityUtil.getScoreIncrement(authorType, InteractionType.COMMENT);
        stringRedisTemplate.opsForValue().increment(scoreKey, score);
    }

    // handle bot count on post
    public void handleBotCount(Long postId) {

        String botCountKey = "post:" + postId + ":bot_count";

        Long count = stringRedisTemplate.opsForValue().increment(botCountKey);

        if (count != null && count > MAX_BOT_REPLIES) {
            throw new TooManyBotRepliesException("Bot reply limit exceeded max 100 allowed.");
        }
    }

    // handle bot interaction with same user and set cooldown
    public void handleCooldown(Long botId, Post post, Comment parentComment) {

        Long humanId = null;

        if (parentComment != null && parentComment.getAuthorType() == AuthorType.USER) {
            humanId = parentComment.getAuthorId();
        } else if (parentComment == null && post.getAuthorType() == AuthorType.USER){
            humanId = post.getAuthorId();
        }

        if (humanId != null) {
            String coolDownKey = "cooldown:bot_" + botId + ":human_" + humanId;

            Boolean success = stringRedisTemplate.opsForValue()
                    .setIfAbsent(coolDownKey, "1", BOT_COOLDOWN_TTL, TimeUnit.SECONDS);

            if (Boolean.FALSE.equals(success)) {
                throw new BotCoolDownException("Bot already interact with this user recently");
            }
        }
    }
}
