package com.example.library.factory;

import org.springframework.stereotype.Component;

import com.example.library.controllers.errorhandling.BadUserDetailsException;
import com.example.library.models.Admin;
import com.example.library.models.Librarian;
import com.example.library.models.LibraryUser;
import com.example.library.models.Reader;

@Component
public class LibraryUserFactory {

	public LibraryUser createUser(String role, String username, String password) {
		LibraryUser user;
		if (role == LibraryUser.READER) {
			user = new Reader(username, password);
		} else if (role == LibraryUser.LIBRARIAN) {
			user = new Librarian(username, password);
		} else if (role == LibraryUser.ADMIN) {
			user = new Admin(username, password);
		} else {
			throw new BadUserDetailsException("role");
		}
		return user;
	}

	public LibraryUser createUser(String role) {
		return createUser(role, null, null);
	}
}
