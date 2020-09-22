package com.example.library.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.library.controllers.errorhandling.BookNotFoundException;
import com.example.library.controllers.errorhandling.QuantityExceededException;
import com.example.library.models.Author;
import com.example.library.models.Book;
import com.example.library.models.BookOrderColumns;
import com.example.library.models.Genre;
import com.example.library.models.SortingOrder;
import com.example.library.repos.BookRepository;

@Service
@Transactional
public class BookService {

	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private AuthorService authorService;
	@Autowired
	private BookService self;

	/* @Cacheable("book") */
	public Book findById(Long id) {
		Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
		return book;
	}

	@Cacheable("booksByCriteria")
	public List<Book> findBooks(String name, Genre genre, String author, BookOrderColumns orderBy, SortingOrder order) {
		List<Book> books = bookRepository.findBooks(name, genre, author, orderBy, order);
		return books;
	}

	@Caching(/* put = { @CachePut(value = "book", key = "#result.bookId") }, */ evict = {
			@CacheEvict(value = "booksByCriteria", allEntries = true) })
	public Book addBook(@RequestBody Book book) {
		Set<Author> exactAuthors = authorService.getExactAuthors(book);
		Book foundBook = bookRepository.findExactBook(book, exactAuthors);
		if (foundBook != null) {
			foundBook.setQuantity(foundBook.getQuantity() + book.getQuantity());
			book = foundBook;
		} else {
			book.setAuthors(exactAuthors);
		}
		return bookRepository.save(book);
	}

	@Caching(evict = { /* @CacheEvict(value = "book", key = "#id"), */
			@CacheEvict(value = "booksByCriteria", allEntries = true) })
	public void deleteBook(Long id, Integer quantity) {
		Book book = self.findById(id);
		if (quantity == null) {
			bookRepository.deleteById(id);
		} else {
			decreaseBooks(book, quantity);
		}
	}

	private void decreaseBooks(Book book, Integer quantity) {

		if (quantity > book.getQuantity()) {
			throw new QuantityExceededException(quantity, book.getQuantity());
		} else if (quantity == book.getQuantity()) {
			bookRepository.deleteById(book.getBookId());
		} else {
			book.setQuantity(book.getQuantity() - quantity);
			bookRepository.save(book);
		}
	}

	@Caching(/* put = { @CachePut(value = "book", key = "#id") }, */ evict = {
			@CacheEvict(value = "booksByCriteria", allEntries = true) })
	public Book replaceBook(Long id, Book book) {
		return bookRepository.findById(id).map(oldBook -> {
			oldBook.setBookName(book.getBookName());
			oldBook.setGenre(book.getGenre());
			oldBook.setQuantity(book.getQuantity());
			oldBook.setAuthors(authorService.getExactAuthors(book));
			return bookRepository.save(oldBook);
		}).orElseGet(() -> {
			book.setAuthors(authorService.getExactAuthors(book));
			book.setBookId(id);
			return bookRepository.save(book);
		});
	}
}
