package com.example.library.models;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role")
@Cacheable(true)
public abstract class LibraryUser {

	public final static String LIBRARIAN = "LIBRARIAN";
	public final static String READER = "READER";
	public final static String ADMIN = "ADMIN";

	@Id
	@GeneratedValue
	private Long userId;
	private String username;
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	@OneToMany(mappedBy = "reader", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<Borrowing> borrowings = new HashSet<>();

	public LibraryUser() {
		super();
	}

	public LibraryUser(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

//	public LibraryUser display() {
//		LibraryUser userToDisplay = new LibraryUser();
//		userToDisplay.setUserId(userId);
//		userToDisplay.setUsername(username);
//		userToDisplay.setRole(role);
//		return userToDisplay;
//	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public abstract String getRole();

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Borrowing> getBorrowings() {
		return borrowings;
	}

	public void setBorrowings(Set<Borrowing> borrowings) {
		clearBorrowings();
		for (Borrowing borrowing : borrowings) {
			addBorrowing(borrowing);
		}
	}

	public void addBorrowing(Borrowing borrowing) {
		if (borrowings.contains(borrowing)) {
			return;
		}
		borrowings.add(borrowing);
		borrowing.setReader(this);
	}

	public void removeBorrowing(Borrowing borrowing) {
		if (!borrowings.contains(borrowing)) {
			return;
		}
		borrowings.remove(borrowing);
		if (borrowing.getReader() == this) {
			borrowing.setReader(null);
		}
	}

	public void clearBorrowings() {
		Iterator<Borrowing> iterator = borrowings.iterator();
		while (iterator.hasNext()) {
			Borrowing borrowing = iterator.next();
			iterator.remove();
			borrowing.setReader(null);
		}
	}

	public abstract List<LibraryUser> filterDisplayUsers(List<LibraryUser> users);

	public abstract boolean canManageUser(LibraryUser user);

	public abstract boolean exceedsLimit(int amount);
}
