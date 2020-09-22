package com.example.library.repos;

import java.util.List;

import com.example.library.models.Author;

public interface AuthorSearchRepository {

	List<Author> findAuthorsByName(String name);

	Author findExactAuthor(String name);
}
