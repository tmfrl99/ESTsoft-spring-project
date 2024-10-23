package com.estsoft.springproject.book.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class BookDTO {
    private String id;
    private String name;
    private String author;

    public BookDTO(Book book) {
        id = book.getId();
        name = book.getName();
        author = book.getAuthor();
    }
}
