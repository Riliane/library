package com.example.library.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import com.example.library.models.Borrowing;

@Component
public class BorrowingModelAssembler {

	public EntityModel<Borrowing> toModel(Borrowing borrowing) {
		List<Link> links = new ArrayList<>();
		links.add(linkTo(methodOn(BookController.class).bookById(borrowing.getBook().getBookId())).withRel("book"));
		links.add(linkTo(
				methodOn(UserManagementController.class).userByUsername(null, borrowing.getReader().getUsername()))
						.withRel("user"));
		links.add(linkTo(
				methodOn(UserManagementController.class).userBorrowings(null, borrowing.getReader().getUsername()))
						.withRel("user_borrowings"));
		if (borrowing.getEndDate() == null || borrowing.getEndDate().after(new Date())) {
			links.add(linkTo(methodOn(BookController.class).returnByBorrowing(borrowing.getBorrowingId(), null))
					.withRel("return"));
		}
		return EntityModel.of(borrowing, links);
	}

}
