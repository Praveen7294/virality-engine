package com.praveen.guardrail.virality_engine.service;

import com.praveen.guardrail.virality_engine.dto.CommentRequestDTO;
import com.praveen.guardrail.virality_engine.dto.CommentResponseDTO;
import com.praveen.guardrail.virality_engine.entity.Comment;
import com.praveen.guardrail.virality_engine.entity.Post;
import com.praveen.guardrail.virality_engine.exception.CommentDepthLimitExceededException;
import com.praveen.guardrail.virality_engine.exception.CommentNotFoundException;
import com.praveen.guardrail.virality_engine.exception.CommentPostMismatchException;
import com.praveen.guardrail.virality_engine.exception.PostNotFoundException;
import com.praveen.guardrail.virality_engine.mapper.CommentMapper;
import com.praveen.guardrail.virality_engine.repository.CommentRepository;
import com.praveen.guardrail.virality_engine.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    private final static int MAX_COMMENT_DEPTH_LEVEL = 20;

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public CommentResponseDTO addComment(Long postId, CommentRequestDTO commentRequestDTO) {

        // validate post exits
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException("Post not found with this ID: " + postId));

        Comment parentComment = null;
        int depthLevel = 1;

        if (commentRequestDTO.getParentCommentId() != null) {
            parentComment = commentRepository.findById(commentRequestDTO.getParentCommentId()).orElseThrow(
                    () -> new CommentNotFoundException(
                            "Comment not found with this ID: " + commentRequestDTO.getParentCommentId()));

            depthLevel = parentComment.getDepthLevel() + 1;

            // validate depth level
            if (depthLevel > MAX_COMMENT_DEPTH_LEVEL) {
                throw new CommentDepthLimitExceededException(
                        "Comment depth level limit exceeded, max 20 allowed.");
            }

            // post id validation with post id in comment
            if (!parentComment.getPostId().equals(postId)) {
                throw new CommentPostMismatchException("Parent comment does not belong to this post.");
            }
        }

        Comment comment = CommentMapper.toEntity(post.getId(), depthLevel, parentComment, commentRequestDTO);

        Comment savedComment = commentRepository.save(comment);

        return CommentMapper.toDTO(savedComment);
    }
}
