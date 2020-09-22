package com.example.library.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.library.models.Author;

public interface AuthorRepository extends JpaRepository<Author, Long>, AuthorSearchRepository {

}
