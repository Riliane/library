package com.example.library;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.library.factory.LibraryUserFactory;
import com.example.library.models.LibraryUser;
import com.example.library.repos.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {
	// ...
	@Autowired
	private UserRepository userRepository;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	private LibraryUserFactory userFactory;

	@Override
	public void run(String... args) throws Exception {
		LibraryUser user1 = userFactory.createUser(LibraryUser.ADMIN, "user", passwordEncoder.encode("password"));

		userRepository.save(user1);
	}
}