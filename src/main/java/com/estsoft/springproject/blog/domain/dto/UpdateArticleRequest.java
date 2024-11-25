package com.estsoft.springproject.blog.domain.dto;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateArticleRequest {
	private String title;
	private String content;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UpdateArticleRequest that = (UpdateArticleRequest)o;
		return Objects.equals(title, that.title) &&
			Objects.equals(content, that.content);
	}

	@Override
	public int hashCode() {
		return Objects.hash(title, content);
	}
}
