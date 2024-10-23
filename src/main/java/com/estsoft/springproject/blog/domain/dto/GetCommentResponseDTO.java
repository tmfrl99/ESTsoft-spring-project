package com.estsoft.springproject.blog.domain.dto;

import com.estsoft.springproject.blog.domain.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class GetCommentResponseDTO {
    private Long commentId;
    private Long articleId;
    private String body;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public GetCommentResponseDTO(Comment comment) {
        this.commentId = comment.getId();
        this.articleId = comment.getArticle().getId();
        this.body = comment.getBody();
        this.createdAt = comment.getCreatedAt();
    }
}
