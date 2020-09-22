package com.example.library.repos;

import com.example.library.models.LibraryUser;

public interface UserSearchRepository {
	LibraryUser findMostActiveReader();
}
