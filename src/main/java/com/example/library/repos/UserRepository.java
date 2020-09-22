package com.example.library.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.library.models.LibraryUser;

public interface UserRepository extends JpaRepository<LibraryUser, Long>, UserSearchRepository {

	Optional<LibraryUser> findByUsername(String username);
}
