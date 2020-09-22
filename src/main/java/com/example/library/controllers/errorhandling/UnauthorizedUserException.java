package com.example.library.controllers.errorhandling;

public class UnauthorizedUserException extends RuntimeException {

	public UnauthorizedUserException(String role) {
		super("Not authorized to manage users with role" + role);
	}

	public UnauthorizedUserException() {
		super("Not authorized to perform this action");
	}
}
