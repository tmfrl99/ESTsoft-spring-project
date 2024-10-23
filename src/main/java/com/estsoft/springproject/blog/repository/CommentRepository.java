package com.estsoft.springproject.blog.repository;

import com.estsoft.springproject.blog.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticleId(Long ArticleId);
}
