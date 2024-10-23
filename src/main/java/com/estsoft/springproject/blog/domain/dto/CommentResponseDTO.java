package com.estsoft.springproject.blog.domain.dto;

import com.estsoft.springproject.blog.domain.Article;
import com.estsoft.springproject.blog.domain.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponseDTO {
    private Long commentId;
    private Long articleId;
    private String body;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private ArticleResponse article;

    public CommentResponseDTO(Comment comment) {
        Article articleFromComment = comment.getArticle();

        this.commentId = comment.getId();
        this.articleId = articleFromComment.getId();
        this.body = comment.getBody();
        this.createdAt = comment.getCreatedAt();
        this.article = new ArticleResponse(articleFromComment);
    }
}
