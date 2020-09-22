package com.example.library.controllers.errorhandling;

public class BookNotFoundException extends RuntimeException {

	public BookNotFoundException(Long id) {
		super("Could not find book " + id);
	}

}
