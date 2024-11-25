package com.estsoft.springproject.book.controller;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.estsoft.springproject.book.domain.Book;
import com.estsoft.springproject.book.domain.BookDTO;
import com.estsoft.springproject.book.repository.BookRepository;
import com.estsoft.springproject.book.service.BookService;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest2 {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private BookService bookService;

	@BeforeEach
	public void mockMvcSetUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@AfterEach
	public void cleanUp() {
		bookRepository.deleteAll();
	}

	// GET /books 책 전체 목록 조회
	@Test
	@Order(1)
	public void testGetAllBooks() throws Exception {
		// given

		// when
		ResultActions resultActions = mockMvc.perform(get("/books"));

		// then
		resultActions.andExpect(status().isOk())
			.andExpect(view().name("bookManagement"))
			.andExpect(model().attribute("bookList", hasSize(0)));
	}

	@Test
	public void showById() throws Exception {
		// given
		Book book = bookService.findBy("첫번째");

		// when
		ResultActions resultActions = mockMvc.perform(get("/books/{id}", "첫번째").accept(MediaType.APPLICATION_JSON));

		// then
		resultActions.andExpect(status().isOk())
			.andExpect(view().name("bookDetail"))
			.andExpect(model().attribute("book", samePropertyValuesAs(new BookDTO(book))));
	}

	@Test
	public void addBook() throws Exception {
		// given
		String id = "id";
		String title = "title";
		String author = "author";

		// when
		ResultActions resultActions = mockMvc.perform(post("/books")
			.param("id", id)
			.param("name", title)
			.param("author", author)
			.accept(MediaType.APPLICATION_JSON));

		// then
		resultActions.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/books"));

		Book savedBook = bookRepository.findById(id).orElse(null);
		assertThat(savedBook).isNotNull();
		assertThat(savedBook.getName()).isEqualTo(title);
		assertThat(savedBook.getAuthor()).isEqualTo(author);
	}
}