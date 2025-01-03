package com.estsoft.springproject.book.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.estsoft.springproject.book.domain.Book;
import com.estsoft.springproject.book.domain.BookDTO;
import com.estsoft.springproject.book.service.BookService;

@Controller
@RequestMapping("/books")
public class BookController {

	private final BookService bookService;

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@GetMapping
	public String showAll(Model model) {
		List<BookDTO> bookList = bookService.findAll()
			.stream()
			.map(BookDTO::new)
			.toList();

		model.addAttribute("bookList", bookList);
		return "bookManagement";
	}

	@GetMapping("/{id}")
	public String showOne(@PathVariable String id, Model model) {
		Book book = bookService.findBy(id);
		model.addAttribute("book", new BookDTO(book));
		return "bookDetail";
	}

	@PostMapping
	@Transactional
	public String addBook(@RequestParam String id,
		@RequestParam String name,
		@RequestParam String author) {

		bookService.saveOne(new Book(id, name, author));
		return "redirect:/books";   // GET/books    3xx
	}
}
