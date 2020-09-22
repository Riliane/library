package com.example.library.controllers.errorhandling;

public class BadUserDetailsException extends RuntimeException {
	public BadUserDetailsException(String badDetail) {
		super("Invalid user details: Invalid " + badDetail);
	}
}
