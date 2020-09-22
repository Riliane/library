package com.example.library.repos;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.library.models.Author;
import com.example.library.models.Book;
import com.example.library.models.BookOrderColumns;
import com.example.library.models.Genre;
import com.example.library.models.SortingOrder;

@Repository
public class BookSearchRepositoryImpl implements BookSearchRepository {

	@Autowired
	EntityManager em;

	@Override
	public List<Book> findBooks(String name, Genre genre, String author, BookOrderColumns sortingColumn,
			SortingOrder order) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Book> cq = cb.createQuery(Book.class);
		List<Predicate> predicates = new ArrayList<>();
		Root<Book> book = cq.from(Book.class);
		if (author != null) {
			// Join<Book, Author> join = book.join("authors", JoinType.INNER);
			// from = join;
			predicates.add(cb.like(book.join("authors", JoinType.INNER).get("authorName").as(String.class),
					"%" + author + "%"));
		}
		if (name != null) {
			predicates.add(cb.like(book.get("bookName"), "%" + name + "%"));
		}
		if (genre != null) {
			predicates.add(cb.equal(book.get("genre"), genre));
		}

		cq.where(predicates.toArray(new Predicate[0]));

		if (sortingColumn != null && order != null) {
			Path<Object> sortBy = book.get(sortingColumn.getName());
			if (order == SortingOrder.ASC) {
				return em.createQuery(cq.orderBy(cb.asc(sortBy))).getResultList();
			}
			return em.createQuery(cq.orderBy(cb.desc(sortBy))).getResultList();
		}

		return em.createQuery(cq).getResultList();
	}

	public Book findExactBook(Book newBook) {
		return findExactBook(newBook, newBook.getAuthors());
	}

	public Book findExactBook(Book newBook, Set<Author> exactAuthors) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Book> cq = cb.createQuery(Book.class);
		List<Predicate> predicates = new ArrayList<>();
		Root<Book> book = cq.from(Book.class);
		if (exactAuthors != null && !exactAuthors.isEmpty()) {
			for (Author author : exactAuthors) {
				if (author.getAuthorId() != null) { // exclude the new authors, else it's trying to use new authors
													// before they are saved
					predicates.add(cb.isMember(author, book.get("authors")));
				}
			}
			int existingAuthors = predicates.size(); // we only have 1 predicate for each author now
			predicates.add(cb.equal(cb.size(book.get("authors")), existingAuthors));
		}
		if (newBook.getBookName() != null) {
			predicates.add(cb.equal(book.get("bookName"), newBook.getBookName()));
		}
		if (newBook.getGenre() != null) {
			predicates.add(cb.equal(book.get("genre"), newBook.getGenre()));
		}

		cq.where(predicates.toArray(new Predicate[0]));

		List<Book> list = em.createQuery(cq).getResultList();
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public Book findMostBorrowedBook() {
		List<Book> list = em.createQuery("SELECT b FROM Book b ORDER BY borrowings.size DESC", Book.class)
				.setMaxResults(1).getResultList();
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
}
