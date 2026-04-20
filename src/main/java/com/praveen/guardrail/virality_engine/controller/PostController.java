package com.praveen.guardrail.virality_engine.controller;

import com.praveen.guardrail.virality_engine.dto.PostRequestDTO;
import com.praveen.guardrail.virality_engine.dto.PostResponseDTO;
import com.praveen.guardrail.virality_engine.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@Valid @RequestBody PostRequestDTO postRequestDTO) {

        PostResponseDTO postResponseDTO = postService.createPost(postRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(postResponseDTO);
    }
}
