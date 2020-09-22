package com.example.library.controllers.errorhandling;

public class BadSortingException extends RuntimeException {

	public BadSortingException() {
		super("Must specify both column and order to sort");
	}

}
