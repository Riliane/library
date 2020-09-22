package com.example.library.models;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Cacheable(true)
public class Borrowing {
	@Id
	@GeneratedValue
	private Long borrowingId;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH,
			CascadeType.REFRESH })
	@JoinColumn(name = "userId")
	private LibraryUser reader;
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH,
			CascadeType.REFRESH })
	@JoinColumn(name = "bookId")
	private Book book;
	private Date startDate;
	private Date endDate;

	public Borrowing() {
		super();
	}

	public Borrowing(LibraryUser reader, Book book) {
		super();
		this.reader = reader;
		reader.addBorrowing(this);
		this.book = book;
		book.addBorrowing(this);
		this.startDate = new Date();
	}

	public Long getBorrowingId() {
		return borrowingId;
	}

	public void setBorrowingId(Long borrowingId) {
		this.borrowingId = borrowingId;
	}

	public LibraryUser getReader() {
		return reader;
	}

	public void setReader(LibraryUser reader) {
		if (this.reader == reader) {
			return;
		}
		if (this.reader != null) {
			this.reader.removeBorrowing(this);
		}
		this.reader = reader;
		if (reader != null) {
			reader.addBorrowing(this);
		}

	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		if (this.book == book) {
			return;
		}
		if (this.book != null) {
			this.book.removeBorrowing(this);
		}
		this.book = book;
		if (book != null) {
			book.addBorrowing(this);
		}
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
