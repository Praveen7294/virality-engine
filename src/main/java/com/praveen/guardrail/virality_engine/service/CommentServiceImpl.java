package com.praveen.guardrail.virality_engine.service;

import com.praveen.guardrail.virality_engine.dto.CommentRequestDTO;
import com.praveen.guardrail.virality_engine.dto.CommentResponseDTO;
import com.praveen.guardrail.virality_engine.entity.AuthorType;
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
    private final ViralityService viralityService;
    private final NotificationService notificationService;

    public CommentServiceImpl(
            CommentRepository commentRepository,
            PostRepository postRepository,
            ViralityService viralityService,
            NotificationService notificationService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.viralityService = viralityService;
        this.notificationService = notificationService;
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

            // post id validation with post id of comment
            if (!parentComment.getPostId().equals(postId)) {
                throw new CommentPostMismatchException("Parent comment does not belong to this post.");
            }
        }

        if (commentRequestDTO.getAuthorType() == AuthorType.BOT) {
            viralityService.handleCooldown(commentRequestDTO.getAuthorId(), post, parentComment);
            viralityService.handleBotCount(postId);
        }

        Comment comment = CommentMapper.toEntity(post.getId(), depthLevel, parentComment, commentRequestDTO);

        Comment savedComment = commentRepository.save(comment);

        if (commentRequestDTO.getAuthorType() == AuthorType.BOT && post.getAuthorType() == AuthorType.USER) {
            String message = "Bot " + commentRequestDTO.getAuthorId() + " replied to your post";
            notificationService.handleBotInteraction(post.getAuthorId(), message);
        }

        // increase virality score
        viralityService.handleComment(postId, commentRequestDTO.getAuthorType());

        return CommentMapper.toDTO(savedComment);
    }
}
