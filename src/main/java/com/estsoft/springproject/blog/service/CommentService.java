package com.estsoft.springproject.blog.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estsoft.springproject.blog.domain.Article;
import com.estsoft.springproject.blog.domain.Comment;
import com.estsoft.springproject.blog.domain.dto.ArticleResponse;
import com.estsoft.springproject.blog.domain.dto.CommentRequestDTO;
import com.estsoft.springproject.blog.repository.BlogRepository;
import com.estsoft.springproject.blog.repository.CommentRepository;

@Service
public class CommentService {
	private final CommentRepository commentRepository;
	private final BlogRepository blogRepository;

	public CommentService(CommentRepository commentRepository, BlogRepository blogRepository) {
		this.commentRepository = commentRepository;
		this.blogRepository = blogRepository;
	}

	public Comment saveComment(CommentRequestDTO request, Long articleId) {
		Article article = blogRepository.findById(articleId).orElseThrow(); // NoSuchElementException
		return commentRepository.save(new Comment(request.getBody(), article));
	}

	public Comment findComment(Long commentId) {
		Optional<Comment> optionalComment = commentRepository.findById(commentId);
		return optionalComment.orElse(new Comment());
	}

	@Transactional
	public Comment updateComment(Long commentId, CommentRequestDTO request) {
		Comment comment = commentRepository.findById(commentId).orElseThrow();
		comment.UpdateCommentBody(request.getBody());
		return commentRepository.save(comment);
	}

	public void deleteComment(Long commentId) {
		commentRepository.deleteById(commentId);
	}

	public ArticleResponse findArticleWithComment(Long articleId) {
		Article article = blogRepository.findById(articleId).orElseThrow();
		List<Comment> comments = commentRepository.findByArticleId(articleId);
		return new ArticleResponse(article, comments);
	}
}
