package com.example.library.repos;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.library.models.Author;

public class AuthorSearchRepositoryImpl implements AuthorSearchRepository {

	@Autowired
	EntityManager em;

	@Override
	public List<Author> findAuthorsByName(String name) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Author> cq = cb.createQuery(Author.class);

		Root<Author> author = cq.from(Author.class);
		List<Predicate> predicates = new ArrayList<>();
		if (name != null) {
			predicates.add(cb.like(author.get("authorName"), "%" + name + "%"));
		}
		cq.where(predicates.toArray(new Predicate[0]));

		return em.createQuery(cq).getResultList();
	}

	@Override
	public Author findExactAuthor(String name) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Author> cq = cb.createQuery(Author.class);

		Root<Author> author = cq.from(Author.class);
		List<Predicate> predicates = new ArrayList<>();
		if (name != null) {
			predicates.add(cb.equal(author.get("authorName"), name));
		}
		cq.where(predicates.toArray(new Predicate[0]));

		List<Author> resultList = em.createQuery(cq).getResultList();
		if (resultList.isEmpty()) {
			return null;
		}
		return resultList.get(0);
	}

}
