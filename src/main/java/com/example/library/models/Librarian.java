package com.example.library.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "Librarian")
@DiscriminatorValue(LibraryUser.LIBRARIAN)
public class Librarian extends LibraryUser {
	protected final static String LIBRARIAN = "LIBRARIAN";

	public Librarian() {
		super();
	}

	public Librarian(String username, String password) {
		super(username, password);
	}

	@Override
	public String getRole() {
		return LIBRARIAN;
	}

	@Override
	public List<LibraryUser> filterDisplayUsers(List<LibraryUser> users) {
		List<LibraryUser> usersToDisplay = new ArrayList<LibraryUser>();
		for (LibraryUser user : users) {
			if (user.getRole() == READER) {
				usersToDisplay.add(user);
			}
		}
		return usersToDisplay;
	}

	@Override
	public boolean canManageUser(LibraryUser user) {
		if (user == this) {
			return true;
		}
		if (user.getRole() == READER) {
			return true;
		}
		return false;
	}

	@Override
	public boolean exceedsLimit(int amount) {
		return false;
	}
}
