package com.estsoft.springproject.blog.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Long id;

	@Column
	private String body;

	@CreatedDate
	@Column
	private LocalDateTime createdAt;

	@ManyToOne
	@JoinColumn(name = "article_id")
	private Article article;

	public Comment(String body, Article article) {
		this.body = body;
		this.article = article;
	}

	public void UpdateCommentBody(String body) {
		this.body = body;
	}
}
