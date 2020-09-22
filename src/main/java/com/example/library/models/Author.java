package com.example.library.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Cacheable(true)
public class Author {

	@Id
	@GeneratedValue
	private Long authorId;
	private String authorName;
	@ManyToMany(mappedBy = "authors", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JsonIgnore
	Set<Book> books = new HashSet<>();

	public Author() {
		super();
	}

	public Author(String authorName) {
		this.authorName = authorName;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long id) {
		this.authorId = id;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String name) {
		this.authorName = name;
	}

	public Set<Book> getBooks() {
		return books;
	}

	public void setBooks(Set<Book> books) {
		this.books = books;
	}

	public void addBook(Book book) {
		if (books.contains(book)) {
			return;
		}
		books.add(book);
		book.addAuthor(this);
	}

	public void removeBook(Book book) {
		if (!books.contains(book)) {
			return;
		}
		books.remove(book);
		book.removeAuthor(this);
	}
}
