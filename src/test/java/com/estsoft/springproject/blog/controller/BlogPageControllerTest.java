package com.estsoft.springproject.blog.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
class BlogPageControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@BeforeEach
	public void mockMvcSetUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	void testShowArticle() throws Exception {
		// given

		// when
		ResultActions resultActions = mockMvc.perform(get("/articles"));

		// then
		resultActions.andExpect(status().isOk())
			.andExpect(view().name("articleList"));
	}

	@Test
	void testShowDetails() throws Exception {
		// given

		// when
		ResultActions resultActions = mockMvc.perform(get("/articles/{id}", 1L));

		// then
		resultActions.andExpect(status().isOk())
			.andExpect(view().name("article"));
	}

	@Test
	void testAddArticle() throws Exception {
		// given

		// when
		ResultActions resultActions = mockMvc.perform(get("/new-article"));

		// then
		resultActions.andExpect(status().isOk())
			.andExpect(view().name("newArticle"));
	}

	@Test
	void testModifyArticle() throws Exception {
		// given

		// when
		ResultActions resultActions = mockMvc.perform(get("/new-article")
			.param("id", "1"));

		// then
		resultActions.andExpect(status().isOk())
			.andExpect(view().name("newArticle"));
	}

	@Test
	void testPage() throws Exception {
		// given

		// when
		ResultActions resultActions = mockMvc.perform(get("/thymeleaf/example"));

		// then
		resultActions.andExpect(status().isOk())
			.andExpect(view().name("examplePage"));
	}
}