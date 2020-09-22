package com.example.library.repos;

import java.util.List;
import java.util.Set;

import com.example.library.models.Author;
import com.example.library.models.Book;
import com.example.library.models.BookOrderColumns;
import com.example.library.models.Genre;
import com.example.library.models.SortingOrder;

public interface BookSearchRepository {

	List<Book> findBooks(String name, Genre genre, String author, BookOrderColumns sortingColumn, SortingOrder order);

	Book findExactBook(Book newBook);

	Book findExactBook(Book newBook, Set<Author> exactAuthors);

	Book findMostBorrowedBook();

}
