package com.example.library.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.library.models.Book;
import com.example.library.models.LibraryUser;
import com.example.library.repos.BookRepository;
import com.example.library.repos.UserRepository;

@RestController
public class StatsController {

	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/stats")
	public ResponseEntity getStats() {
		Map<String, EntityModel> model = new HashMap<>();
		Book mostBorrowedBook = bookRepository.findMostBorrowedBook();
		if (mostBorrowedBook != null) {
			model.put("Most borrowed book", EntityModel.of(mostBorrowedBook.getBookName(),
					linkTo(methodOn(BookController.class).bookById(mostBorrowedBook.getBookId())).withRel("book")));
		}
		LibraryUser mostActiveReader = userRepository.findMostActiveReader();
		if (mostActiveReader != null) {
			model.put("Most active reader", EntityModel.of(mostActiveReader.getUsername(), linkTo(
					methodOn(UserManagementController.class).userByUsername(null, mostActiveReader.getUsername()))
							.withRel("reader")));
		}
		EntityModel<Map<String, EntityModel>> collection = EntityModel.of(model,
				linkTo(methodOn(StatsController.class).getStats()).withSelfRel());
		return ResponseEntity.ok(collection);
	}

}
