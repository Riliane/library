package com.example.library.models;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Cacheable(true)
public class Book {

	@Id
	@GeneratedValue
	private Long bookId;
	private String bookName;
	@Enumerated(EnumType.STRING)
	private Genre genre;
	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH,
			CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinTable(name = "books_authors", joinColumns = { @JoinColumn(name = "bookId") }, inverseJoinColumns = {
			@JoinColumn(name = "authorId") })
	private Set<Author> authors = new HashSet<>();
	private int quantity;
	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<Borrowing> borrowings = new HashSet<>();

	public Book() {

	}

	public Book(String bookName) {
		this.bookName = bookName;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long id) {
		this.bookId = id;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String name) {
		this.bookName = name;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	public Set<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(Set<Author> authors) {
		clearAuthors();
		for (Author author : authors) {
			addAuthor(author);
		}
	}

	public void clearAuthors() {
		Iterator<Author> iterator = authors.iterator();
		while (iterator.hasNext()) {
			Author author = iterator.next();
			iterator.remove();
			author.removeBook(this);
		}
		// for (Author author : authors) {
		// removeAuthor(author);
		// }
	}

	public void addAuthor(Author author) {
		if (authors.contains(author)) {
			return;
		}
		authors.add(author);
		author.addBook(this);
	}

	public void removeAuthor(Author author) {
		if (!authors.contains(author)) {
			return;
		}
		authors.remove(author);
		author.removeBook(this);
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
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
		borrowing.setBook(this);
	}

	public void removeBorrowing(Borrowing borrowing) {
		if (!borrowings.contains(borrowing)) {
			return;
		}
		borrowings.remove(borrowing);
		if (borrowing.getBook() == this) {
			borrowing.setBook(null);
		}
	}

	public void clearBorrowings() {
		Iterator<Borrowing> iterator = borrowings.iterator();
		while (iterator.hasNext()) {
			Borrowing borrowing = iterator.next();
			iterator.remove();
			borrowing.setBook(null);
		}
	}

}
