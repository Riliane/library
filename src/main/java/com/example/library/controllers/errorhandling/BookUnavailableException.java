package com.example.library.controllers.errorhandling;

public class BookUnavailableException extends RuntimeException {

	public BookUnavailableException(Long id) {
		super("Book " + id + " out of stock");
	}
}
