package com.example.library.controllers.errorhandling;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.library.security.InvalidJwtAuthenticationException;

@ControllerAdvice
public class UserControllerAdvice {

	@ResponseBody
	@ExceptionHandler(UsernameNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String userNotFoundHandler(UsernameNotFoundException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@ExceptionHandler(BadUserDetailsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String badUserDetailsHandler(BadUserDetailsException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@ExceptionHandler(UnauthorizedUserException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	String unauthorizedUserHandler(UnauthorizedUserException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@ExceptionHandler(InvalidJwtAuthenticationException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	String invalidAuthHandler(InvalidJwtAuthenticationException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@ExceptionHandler(BadCredentialsException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	String badCredentialsHandler(BadCredentialsException ex) {
		return ex.getMessage();
	}
}
