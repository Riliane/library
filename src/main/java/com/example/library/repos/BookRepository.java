package com.example.library.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.library.models.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, BookSearchRepository {

}
