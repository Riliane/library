package com.example.library.repos;

import java.util.List;

import com.example.library.models.Book;
import com.example.library.models.Borrowing;
import com.example.library.models.LibraryUser;

public interface BorrowingSearchRepository {

	List<Borrowing> findAllActive();

	List<Borrowing> findActiveForBook(Book book);

	List<Borrowing> findActiveForReader(LibraryUser reader);

	List<Borrowing> findActive(Book book, LibraryUser reader);
}
