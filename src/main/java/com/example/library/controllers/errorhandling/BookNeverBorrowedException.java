package com.example.library.controllers.errorhandling;

public class BookNeverBorrowedException extends RuntimeException {

	public BookNeverBorrowedException(Long id, String username) {
		super("Book " + id + " has never been borrowed by user " + username + " or has already been returned");
	}

}
