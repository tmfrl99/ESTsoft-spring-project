package com.estsoft.springproject.blog.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import com.estsoft.springproject.blog.domain.Comment;
import com.estsoft.springproject.blog.domain.dto.CommentRequestDTO;
import com.estsoft.springproject.blog.service.BlogService;
import com.estsoft.springproject.blog.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CommentControllerTest {
	private final ObjectMapper objectMapper = new ObjectMapper();
	MockMvc mockMvc;
	@Mock
	private CommentService commentService;

	@Mock
	private BlogService blogService;

	@InjectMocks
	private CommentController commentController;

	@BeforeEach
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
	}

	@Test
	void testSaveComment() throws Exception {
		// given
		Long articleId = 1L;
		Article article = new Article("title", "content");

		CommentRequestDTO request = new CommentRequestDTO();
		request.setBody("This is a test comment.");

		Comment comment = new Comment("This is a test comment.", article);

		when(blogService.findBy(articleId)).thenReturn(article);
		when(commentService.saveComment(any(CommentRequestDTO.class), eq(articleId))).thenReturn(comment);

		// when
		ResultActions resultActions = mockMvc.perform(post("/api/articles/{articleId}/comments", articleId)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		resultActions.andExpect(status().isCreated())
			.andExpect(jsonPath("$.body").value(comment.getBody()));

		verify(commentService, times(1)).saveComment(any(CommentRequestDTO.class), eq(articleId));
	}

	@Test
	void testFindComment() throws Exception {
		// given
		Long commentId = 1L;
		String body = "Test comment";
		Article article = new Article("title", "content");

		Mockito.doReturn(new Comment(body, article))
			.when(commentService)
			.findComment(commentId);

		// when
		ResultActions resultActions = mockMvc.perform(get("/api/comments/{commentId}", commentId));

		// then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("$.body").value(body));

		verify(commentService, times(1)).findComment(commentId);
	}

	@Test
	void testUpdateComment() throws Exception {
		// given
		Long commentId = 1L;
		String body = "Updated Comment";
		CommentRequestDTO request = new CommentRequestDTO();
		request.setBody(body);

		Article article = new Article("title", "content");
		Comment comment = new Comment(body, article);

		when(commentService.updateComment(eq(commentId), any(CommentRequestDTO.class))).thenReturn(comment);

		// when
		ResultActions resultActions = mockMvc.perform(put("/api/comments/{commentId}", 1L)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)));

		// then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("$.body").value(body));

		verify(commentService, times(1)).updateComment(commentId, request);
	}

	@Test
	void testDeleteComment() throws Exception {
		// given
		Long commentId = 1L;

		// when
		ResultActions resultActions = mockMvc.perform(delete("/api/comments/{commentId}", commentId));

		// then
		resultActions.andExpect(status().isOk());

		verify(commentService, times(1)).deleteComment(commentId);
	}
}