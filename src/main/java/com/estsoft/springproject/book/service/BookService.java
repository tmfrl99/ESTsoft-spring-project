package com.estsoft.springproject.book.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.estsoft.springproject.book.domain.Book;
import com.estsoft.springproject.book.repository.BookRepository;

@Service
public class BookService {
	private final BookRepository bookRepository;

	public BookService(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	public List<Book> findAll() {
		return bookRepository.findAll(Sort.by("id"));   // 오름차순
		// select * from book order by id;
	}

	public Book findBy(String id) {
		return bookRepository.findById(id).orElse(new Book());
	}

	public Book saveOne(Book book) {
		return bookRepository.save(book);
	}
}
