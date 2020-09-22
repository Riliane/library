package com.example.library.controllers.errorhandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class BookControllerAdvice {

	@ResponseBody
	@ExceptionHandler(BookNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String bookNotFoundHandler(BookNotFoundException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@ExceptionHandler(QuantityExceededException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String quantityExceededHandler(QuantityExceededException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@ExceptionHandler(BookUnavailableException.class)
	@ResponseStatus(HttpStatus.LOCKED)
	String bookUnavailableHandler(BookUnavailableException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@ExceptionHandler(BookNeverBorrowedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String bookNeverBorrowedHandler(BookNeverBorrowedException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@ExceptionHandler(BadSortingException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String badSortingHandler(BadSortingException ex) {
		return ex.getMessage();
	}

}
