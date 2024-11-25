package com.estsoft.springproject.blog.service;

import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.estsoft.springproject.blog.domain.Article;
import com.estsoft.springproject.blog.domain.dto.AddArticleRequest;
import com.estsoft.springproject.blog.domain.dto.UpdateArticleRequest;
import com.estsoft.springproject.blog.repository.BlogRepository;

@ExtendWith(MockitoExtension.class)
class BlogServiceTest {

	@InjectMocks
	BlogService service;

	@Mock
	BlogRepository repository;

	@Test
	void testAllArticles() {
		// given
		List<Article> list = Arrays.asList(
			new Article("title1", "content1"),
			new Article("title2", "content2")
		);

		when(repository.findAll()).thenReturn(list);

		// when
		List<Article> articles = service.findAll();

		// then
		assertThat(articles.size(), is(2));
	}

	@Test
	void testFindOne() {
		// given
		Long id = 1L;
		Article article = new Article("title", "content");
		when(repository.findById(id)).thenReturn(Optional.of(article));

		// when
		Article foundArticle = service.findBy(id);

		// then
		assertThat(foundArticle.getTitle(), is(article.getTitle()));
		verify(repository, times(1)).findById(id);
	}

	@Test
	void testSaveArticle() {
		// given
		String title = "title";
		String content = "content";

		AddArticleRequest request = new AddArticleRequest(title, content);
		Article article = new Article(title, content);

		Mockito.doReturn(article).when(repository).save(Mockito.any(Article.class));

		// when
		Article savedArticle = service.saveArticle(request);

		// then
		assertThat(savedArticle.getTitle(), is(title));
		assertThat(savedArticle.getContent(), is(content));

		verify(repository, times(1)).save(Mockito.any(Article.class));
	}

	@Test
	void testDeleteArticle() {
		// given
		Long id = 1L;

		// when
		service.deleteBy(id);

		// then
		verify(repository, times(1)).deleteById(id);
	}

	@Test
	void testUpdateArticle() {
		// given
		Long id = 1L;
		String title = "Updated_Title";
		String content = "Updated_Content";
		UpdateArticleRequest request = new UpdateArticleRequest(title, content);
		Article article = new Article("title", "content");

		Mockito.when(repository.findById(id)).thenReturn(Optional.of(article));

		// when
		Article updatedArticle = service.update(id, request);

		// then
		assertThat(updatedArticle.getTitle(), is(title));
	}
}