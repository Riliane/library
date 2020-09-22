package com.example.library.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "Reader")
@DiscriminatorValue(LibraryUser.READER)
public class Reader extends LibraryUser {

	private final static int BOOK_LIMIT = 5;

	public Reader() {
		super();
	}

	public Reader(String username, String password) {
		super(username, password);
	}

	@Override
	public String getRole() {
		return READER;
	}

	@Override
	public List<LibraryUser> filterDisplayUsers(List<LibraryUser> users) {
		return new ArrayList<LibraryUser>();
	}

	@Override
	public boolean canManageUser(LibraryUser user) {
		return user == this;
	}

	@Override
	public boolean exceedsLimit(int amount) {
		return (amount >= BOOK_LIMIT);
	}

}
