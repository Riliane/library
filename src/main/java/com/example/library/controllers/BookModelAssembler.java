package com.example.library.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import com.example.library.models.Book;

@Component
public class BookModelAssembler {

	public EntityModel<Book> toModel(Book book) {
		return EntityModel.of(book, linkTo(methodOn(BookController.class).bookById(book.getBookId())).withSelfRel(),
				linkTo(methodOn(BookController.class).borrowBook(book.getBookId(), null)).withRel("borrow"),
				linkTo(methodOn(BookController.class).findBooks(null, null, null, null, null)).withRel("books"));
	}

}
