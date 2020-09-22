package com.example.library.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.library.models.Borrowing;

public interface BorrowingRepository extends JpaRepository<Borrowing, Long>, BorrowingSearchRepository {

}
