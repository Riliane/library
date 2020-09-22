package com.example.library.repos;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.library.models.LibraryUser;

public class UserSearchRepositoryImpl implements UserSearchRepository {

	@Autowired
	EntityManager em;

	@Override
	public LibraryUser findMostActiveReader() {
		List<LibraryUser> list = em
				.createQuery("SELECT u FROM LibraryUser u WHERE u.role = :role ORDER BY borrowings.size DESC",
						LibraryUser.class)
				.setMaxResults(1).setParameter("role", LibraryUser.READER).getResultList();
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);

	}

}
