package com.praveen.guardrail.virality_engine.util;

import com.praveen.guardrail.virality_engine.entity.AuthorType;

public class ViralityUtil {

    public static int getScoreIncrement(AuthorType authorType, InteractionType interactionType) {

        int score;
        if (authorType == AuthorType.USER && interactionType == InteractionType.LIKE) {
            score = 20;
        } else if (authorType == AuthorType.USER && interactionType == InteractionType.COMMENT) {
            score = 50;
        } else if (authorType == AuthorType.BOT && interactionType == InteractionType.COMMENT) {
            score = 1;
        } else {
            score = 0;
        }

        return score;
    }
}
