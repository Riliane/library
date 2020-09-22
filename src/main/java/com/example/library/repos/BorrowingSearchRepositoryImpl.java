package com.example.library.repos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.library.models.Book;
import com.example.library.models.Borrowing;
import com.example.library.models.LibraryUser;

public class BorrowingSearchRepositoryImpl implements BorrowingSearchRepository {

	@Autowired
	EntityManager em;

	@Override
	public List<Borrowing> findAllActive() {
		return findActive(null, null);
	}

	@Override
	public List<Borrowing> findActiveForBook(Book book) {
		return findActive(book, null);
	}

	@Override
	public List<Borrowing> findActiveForReader(LibraryUser reader) {
		return findActive(null, reader);
	}

	public List<Borrowing> findActive(Book book, LibraryUser reader) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Borrowing> cq = cb.createQuery(Borrowing.class);
		List<Predicate> predicates = new ArrayList<>();
		Root<Borrowing> borrowing = cq.from(Borrowing.class);
		if (book != null) {
			predicates.add(cb.equal(borrowing.get("book"), book));
		}
		if (reader != null) {
			predicates.add(cb.equal(borrowing.get("reader"), reader));
		}
		predicates
				.add(cb.or(cb.isNull(borrowing.get("endDate")), cb.greaterThan(borrowing.get("endDate"), new Date())));
		cq.where(predicates.toArray(new Predicate[0]));

		return em.createQuery(cq.orderBy(cb.asc(borrowing.get("startDate")))).getResultList();
	}

}
