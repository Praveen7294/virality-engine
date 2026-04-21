package com.praveen.guardrail.virality_engine.controller;

import com.praveen.guardrail.virality_engine.service.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<String> likePost(@PathVariable Long postId) {

        likeService.likePost(postId);

        return ResponseEntity.status(HttpStatus.OK).body("Post liked successfully");
    }
}
