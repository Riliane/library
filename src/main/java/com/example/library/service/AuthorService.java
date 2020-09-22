package com.example.library.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.library.models.Author;
import com.example.library.models.Book;
import com.example.library.repos.AuthorRepository;

@Service
@Transactional
public class AuthorService {

	@Autowired
	private AuthorRepository authorRepository;

	public Set<Author> getExactAuthors(Book book) {
		Set<Author> authors = book.getAuthors();
		Set<Author> exactAuthors = new HashSet<Author>();
		for (Author author : authors) {
			Author foundAuthor = authorRepository.findExactAuthor(author.getAuthorName());
			if (foundAuthor == null) {
				exactAuthors.add(author);
			} else {
				exactAuthors.add(foundAuthor);
			}
		}
		return exactAuthors;
	}
}
