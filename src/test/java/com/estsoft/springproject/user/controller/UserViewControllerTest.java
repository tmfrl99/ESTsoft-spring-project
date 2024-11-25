package com.estsoft.springproject.user.controller;

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
class UserViewControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@BeforeEach
	public void mockMvcSetUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	void testLogin() throws Exception {
		// given

		// when
		ResultActions resultActions = mockMvc.perform(get("/login"));

		// then
		resultActions.andExpect(status().isOk())
			.andExpect(view().name("login"));
	}

	@Test
	void testSignup() throws Exception {
		// given

		// when
		ResultActions resultActions = mockMvc.perform(get("/signup"));

		// then
		resultActions.andExpect(status().isOk())
			.andExpect(view().name("signup"));
	}

	@Test
	void testLogout() throws Exception {
		// given

		// when
		ResultActions resultActions = mockMvc.perform(get("/logout"));

		// then
		resultActions.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/login"));
	}

	@Test
	void testSaveUser() throws Exception {
		// given
		String email = "email@email.com";
		String password = "password1234";

		// when
		ResultActions resultActions = mockMvc.perform(post("/user")
			.param("email", email)
			.param("password", password));

		// then
		resultActions.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/login"));
	}
}