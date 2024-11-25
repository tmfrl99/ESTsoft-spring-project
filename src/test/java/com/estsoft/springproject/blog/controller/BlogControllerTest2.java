package com.estsoft.springproject.blog.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.estsoft.springproject.blog.domain.Article;
import com.estsoft.springproject.blog.domain.dto.AddArticleRequest;
import com.estsoft.springproject.blog.domain.dto.ArticleResponse;
import com.estsoft.springproject.blog.domain.dto.UpdateArticleRequest;
import com.estsoft.springproject.blog.service.BlogService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
	// @MockitoSettings(strictness = Strictness.LENIENT)
class BlogControllerTest2 {
	@InjectMocks
	BlogController blogController;

	@Mock
	BlogService blogService;

	MockMvc mockMvc;

	@BeforeEach
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(blogController).build();
	}

	// 블로그 게시글 정보 저장 API 테스트
	@Test
	public void testArticleSave() throws Exception {
		// given : API 호출에 필요한 데이터 만들기
		// {
		// 	"title" : "mock_title",
		// 	"content" : "mock_content"
		// }
		// 직렬화 : 객체 -> json(String)
		String title = "mock_title";
		String content = "mock_content";

		AddArticleRequest request = new AddArticleRequest(title, content);
		ObjectMapper objectMapper = new ObjectMapper();
		String articleJson = objectMapper.writeValueAsString(request);

		// stub (service.saveArticle 호출 시 위에서 만든 article을 리턴하도록 stub 처리)
		Mockito.when(blogService.saveArticle(any())).thenReturn(new Article(title, content));

		// when: API 호출
		ResultActions resultActions = mockMvc.perform(
			post("/api/articles")
				.content(articleJson)
				.contentType(MediaType.APPLICATION_JSON)
		);

		// then: 호출 결과 검증
		resultActions.andExpect(status().isCreated())
			.andExpect(jsonPath("title").value(title))
			.andExpect(jsonPath("content").value(content));
	}

	// DELETE /articles/{id} : 블로그 게시글 삭제 API 테스트
	@Test
	public void testArticleDelete() throws Exception {
		// given
		Long id = 1L;

		// when
		ResultActions resultActions = mockMvc.perform(delete("/api/articles/{id}", id));

		// then : HTTP status code 검증, service.deleteBy 호출 횟수 검증
		resultActions.andExpect(status().isOk());

		verify(blogService, times(1)).deleteBy(id);
	}

	// GET /articles/{id} : 블로그 게시글 단건 조회 API 테스트
	@Test
	public void testFindOne() throws Exception {
		// given
		Long id = 1L;

		// stubing: article 객체를 만들어준다.
		Mockito.doReturn(new Article("title", "content"))
			.when(blogService).findBy(id);

		// when
		ResultActions resultActions = mockMvc.perform(get("/api/articles/{id}", id));

		// then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("title").value("title"))
			.andExpect(jsonPath("content").value("content"));

		verify(blogService, times(1)).findBy(id);
	}

	// 블로그 게시글 전체 목록 조회 API 테스트
	@Test
	public void testArticleFindAll() throws Exception {
		// given
		List<ArticleResponse> articles = List.of(
			new ArticleResponse(new Article("title1", "content1")),
			new ArticleResponse(new Article("title2", "content2"))
		);

		Mockito.when(blogService.findAll())
			.thenReturn(articles.stream().map(
					article -> new Article(article.getTitle(), article.getContent())
				)
				.toList());

		// when
		ResultActions resultActions = mockMvc.perform(get("/api/articles"));

		// then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].title").value("title1"))
			.andExpect(jsonPath("$[0].content").value("content1"))
			.andExpect(jsonPath("$[1].title").value("title2"))
			.andExpect(jsonPath("$[1].content").value("content2"));

		verify(blogService, times(1)).findAll();
	}

	// 블로그 게시글 수정 API 테스트
	@Test
	public void testUpdate() throws Exception {
		// given
		Long id = 1L;
		UpdateArticleRequest request = new UpdateArticleRequest("updated_title", "updated_content");

		Mockito.when(blogService.update(eq(id), any(UpdateArticleRequest.class)))
			.thenReturn(new Article("updated_title", "updated_content"));

		// when
		ResultActions resultActions = mockMvc.perform(put("/api/articles/{id}", id)
			.contentType(MediaType.APPLICATION_JSON)
			.content(new ObjectMapper().writeValueAsString(request)));

		// given
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("title").value("updated_title"))
			.andExpect(jsonPath("content").value("updated_content"));

		verify(blogService, times(1)).update(id, request);
	}
}