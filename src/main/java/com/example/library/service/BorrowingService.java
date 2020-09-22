package com.example.library.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.library.controllers.errorhandling.BookNeverBorrowedException;
import com.example.library.controllers.errorhandling.BookNotFoundException;
import com.example.library.controllers.errorhandling.BookUnavailableException;
import com.example.library.controllers.errorhandling.QuantityExceededException;
import com.example.library.models.Book;
import com.example.library.models.Borrowing;
import com.example.library.models.LibraryUser;
import com.example.library.repos.BorrowingRepository;

@Service
@Transactional
public class BorrowingService {

	@Autowired
	private BorrowingRepository borrowingRepository;
	@Autowired
	private BorrowingService self;

	/* @Cacheable("borrowing") */
	public Borrowing findById(Long id) {
		return borrowingRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
	}

	@Caching(evict = { @CacheEvict(value = "borrowingsByBook", key = "#book"),
			@CacheEvict(value = "borrowingsByReader", key = "#user") }/*
																		 * , put = {
																		 * 
																		 * @CachePut(value = "borrowing", key =
																		 * "#result.borrowingId") }
																		 */)
	public Borrowing borrowBook(Book book, LibraryUser user) {
		if (!isBookAvailable(book)) {
			throw new BookUnavailableException(book.getBookId());
		}
		if (isLimitExceeded(user)) {
			throw new QuantityExceededException();
		}
		return borrowingRepository.save(new Borrowing(user, book));
	}

	@Caching(evict = { @CacheEvict(value = "borrowingsByBook", key = "#book"),
			@CacheEvict(value = "borrowingsByReader", key = "#user") }/*
																		 * , put = {
																		 * 
																		 * @CachePut(value = "borrowing", key =
																		 * "#result.borrowingId") }
																		 */)
	public Borrowing returnBook(LibraryUser user, Book book) {
		List<Borrowing> borrowings = borrowingRepository.findActive(book, user);
		if (borrowings.isEmpty()) {
			throw new BookNeverBorrowedException(book.getBookId(), user.getUsername());
		}
		Borrowing borrowing = borrowings.get(0); // return the earliest if there are many
		return returnByBorrowing(borrowing);
	}

	@Caching(evict = { @CacheEvict(value = "borrowingsByBook", key = "#borrowing.book"),
			@CacheEvict(value = "borrowingsByReader", key = "#borrowing.reader") }/*
																					 * , put = {
																					 * 
																					 * @CachePut(value = "borrowing",
																					 * key = "#result.borrowingId") }
																					 */)
	public Borrowing returnByBorrowing(Borrowing borrowing) {
		if (borrowing.getEndDate() != null && borrowing.getEndDate().before(new Date())) {
			throw new BookNeverBorrowedException(borrowing.getBook().getBookId(), borrowing.getReader().getUsername());
		}
		borrowing.setEndDate(new Date());
		return borrowingRepository.save(borrowing);
	}

	private boolean isBookAvailable(Book book) {
		List<Borrowing> activeBorrowings = self.findActiveForBook(book);
		return activeBorrowings.size() < book.getQuantity();
	}

	@Cacheable("borrowingsByBook")
	public List<Borrowing> findActiveForBook(Book book) {
		return borrowingRepository.findActiveForBook(book);
	}

	private boolean isLimitExceeded(LibraryUser user) {
		int amount = self.findActiveForReader(user).size();
		return user.exceedsLimit(amount);
	}

	@Cacheable("borrowingsByReader")
	public List<Borrowing> findActiveForReader(LibraryUser user) {
		return borrowingRepository.findActiveForReader(user);
	}

}
