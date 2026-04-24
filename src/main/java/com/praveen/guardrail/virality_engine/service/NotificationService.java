package com.praveen.guardrail.virality_engine.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class NotificationService {

    private static final int NOTIFICATION_COOLDOWN_TTL = 15;

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final StringRedisTemplate stringRedisTemplate;

    public NotificationService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    // Throttler
    public void handleBotInteraction(Long userId, String message) {

        String cooldownKey = "user:" + userId + ":notif_cooldown";
        String listKey = "user:" + userId + ":pending_notifs";

        Boolean success = stringRedisTemplate.opsForValue()
                .setIfAbsent(cooldownKey, "1", NOTIFICATION_COOLDOWN_TTL, TimeUnit.MINUTES);

        if (Boolean.TRUE.equals(success)) {
            logger.info("Push notification to the user " + userId + ": " + message);
        } else {
            stringRedisTemplate.opsForList().rightPush(listKey, message);
        }
    }

    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void processPendingNotification() {

        Set<String> listKeys = stringRedisTemplate.keys("user:*:pending_notifs");

        if (listKeys != null && !listKeys.isEmpty()) {
            for (String key : listKeys) {

                List<String> message = stringRedisTemplate.opsForList().range(key, 0, -1);

                if (message != null && !message.isEmpty()) {
                    // Example: user:1:pending_notifs -> user, 1, pending_notifs
                    // so taking index 1 here
                    String userId = key.split(":")[1];

                    int count = message.size();

                    String[] parts = message.get(0).split(" ");
                    String firstBot = parts[0] + " " + parts[1];

                    logger.info("Summarized Push Notification: {} and {} others interacted with your posts."
                    , firstBot, count - 1);

                    stringRedisTemplate.delete(key);
                }
            }
        }
    }
}
