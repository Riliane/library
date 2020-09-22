package com.example.library.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.library.models.LibraryUser;
import com.example.library.repos.UserRepository;

@Service
@Transactional
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository repository;

	@Autowired
	private UserService self;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LibraryUser user = self.findByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}

		List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(user.getRole()));

		return new User(user.getUsername(), user.getPassword(), authorities);
	}

	// @Cacheable("user")
	public LibraryUser findByUsername(String username) {
		LibraryUser user = repository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username: " + username + " not found"));
		return user;
	}

	@Caching(evict = { /* @CacheEvict(value = "user", key = "#user.username"), */
			@CacheEvict(value = "users", allEntries = true) })
	public LibraryUser save(LibraryUser user) {
		return repository.save(user);
	}

	@Cacheable("users")
	public List<LibraryUser> findAll() {
		return repository.findAll();
	}
}
