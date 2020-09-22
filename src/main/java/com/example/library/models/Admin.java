package com.example.library.models;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "Admin")
@DiscriminatorValue(LibraryUser.ADMIN)
public class Admin extends LibraryUser {

	public Admin() {
		super();
	}

	public Admin(String username, String password) {
		super(username, password);
	}

	@Override
	public String getRole() {
		return ADMIN;
	}

	@Override
	public List<LibraryUser> filterDisplayUsers(List<LibraryUser> users) {
		return users;
	}

	@Override
	public boolean canManageUser(LibraryUser user) {
		return true;
	}

	@Override
	public boolean exceedsLimit(int amount) {
		return false;
	}

}
