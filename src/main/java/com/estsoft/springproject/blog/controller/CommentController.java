package com.estsoft.springproject.blog.controller;

import com.estsoft.springproject.blog.domain.Comment;
import com.estsoft.springproject.blog.domain.dto.ArticleResponse;
import com.estsoft.springproject.blog.domain.dto.CommentRequestDTO;
import com.estsoft.springproject.blog.domain.dto.CommentResponseDTO;
import com.estsoft.springproject.blog.service.BlogService;
import com.estsoft.springproject.blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;
    private final BlogService blogService;

    public CommentController(CommentService commentService, BlogService blogService) {
        this.commentService = commentService;
        this.blogService = blogService;
    }

    @PostMapping("/articles/{articleId}/comments")
    public ResponseEntity<CommentResponseDTO> saveCommentByArticleId(@PathVariable Long articleId,
                                                                     @RequestBody CommentRequestDTO request) {
        Comment comment = commentService.saveComment(request, articleId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommentResponseDTO(comment));
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponseDTO> findCommentById(@PathVariable Long commentId) {
        Comment comment = commentService.findComment(commentId);
        return ResponseEntity.ok(new CommentResponseDTO(comment));
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponseDTO> updateCommentById(@PathVariable Long commentId,
                                                                @RequestBody CommentRequestDTO request) {
        Comment comment = commentService.updateComment(commentId, request);
        return ResponseEntity.ok(new CommentResponseDTO(comment));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/articles/{articleId}/comments")
    public ResponseEntity<ArticleResponse> findCommentByArticleId(@PathVariable Long articleId) {
        ArticleResponse articleResponse = commentService.findArticleWithComment(articleId);
        return ResponseEntity.ok(articleResponse);
    }
}
