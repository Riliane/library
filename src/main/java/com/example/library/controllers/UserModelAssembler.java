package com.example.library.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import com.example.library.models.LibraryUser;
import com.example.library.models.UserDTO;

@Component
public class UserModelAssembler {

	public EntityModel<UserDTO> toModel(UserDTO userDto) {
		return EntityModel.of(userDto,
				linkTo(methodOn(UserManagementController.class).userByUsername(null, userDto.getUsername()))
						.withSelfRel(),
				linkTo(methodOn(UserManagementController.class).allUsers(null)).withRel("users"));
	}

	public EntityModel<LibraryUser> toModel(LibraryUser user) {
		return EntityModel.of(user,
				linkTo(methodOn(UserManagementController.class).userByUsername(null, user.getUsername())).withSelfRel(),
				linkTo(methodOn(UserManagementController.class).userBorrowings(null, user.getUsername()))
						.withRel("borrowings"),
				linkTo(methodOn(UserManagementController.class).allUsers(null)).withRel("users"));
	}
}
