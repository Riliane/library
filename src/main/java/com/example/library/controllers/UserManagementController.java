package com.example.library.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.library.controllers.errorhandling.BadUserDetailsException;
import com.example.library.controllers.errorhandling.UnauthorizedUserException;
import com.example.library.mapper.UserDTOMapper;
import com.example.library.models.Borrowing;
import com.example.library.models.LibraryUser;
import com.example.library.models.UserDTO;
import com.example.library.repos.UserRepository;
import com.example.library.security.UserService;

@RestController
public class UserManagementController {

	@Autowired
	private UserRepository repository;
	@Autowired
	private UserService userService;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	private UserDTOMapper userDTOMapper;
	@Autowired
	private UserModelAssembler userModelAssembler;
	@Autowired
	private BorrowingModelAssembler borrowingModelAssembler;

	@GetMapping("/me")
	public ResponseEntity currentUser(@AuthenticationPrincipal UserDetails userDetails) {
		Map<Object, Object> map = new HashMap<>();
		map.put("username", userDetails.getUsername());
		map.put("role", getRole(userDetails));
		EntityModel<Map<Object, Object>> model = EntityModel.of(map,
				linkTo(methodOn(UserManagementController.class).currentUser(userDetails)).withSelfRel());
		return ResponseEntity.ok(model);
	}

	@PutMapping("/me")
	public ResponseEntity editSelf(@AuthenticationPrincipal UserDetails userDetails, @RequestBody LibraryUser user) {
		LibraryUser oldUser = getCurrentUser(userDetails);
		overwriteUserData(user, oldUser);
		LibraryUser save = userService.save(oldUser);
		EntityModel<LibraryUser> model = EntityModel.of(save,
				linkTo(methodOn(UserManagementController.class).currentUser(userDetails)).withSelfRel());
		return ResponseEntity.ok(model);
		// log out?
	}

	@GetMapping("/users")
	public ResponseEntity allUsers(@AuthenticationPrincipal UserDetails userDetails) {
		LibraryUser user = getCurrentUser(userDetails);
		List<LibraryUser> users = userService.findAll();
		List<UserDTO> usersToDisplay = userDTOMapper.map(user.filterDisplayUsers(users));
		List<EntityModel<UserDTO>> userModels = usersToDisplay.stream()
				.map(userDto -> userModelAssembler.toModel(userDto)).collect(Collectors.toList());
		CollectionModel<EntityModel<UserDTO>> model = CollectionModel.of(userModels,
				linkTo(methodOn(UserManagementController.class).allUsers(userDetails)).withSelfRel());
		return ResponseEntity.ok(model);
	}

	@GetMapping("/users/{username}")
	public ResponseEntity userByUsername(@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable String username) {
		LibraryUser user = userService.findByUsername(username);
		checkAuthorizedToManage(userDetails, user);
		EntityModel<LibraryUser> model = userModelAssembler.toModel(user);
		return ResponseEntity.ok(model);
	}

	@GetMapping("/users/{username}/borrowings")
	public ResponseEntity userBorrowings(@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable String username) {
		LibraryUser user = userService.findByUsername(username);
		checkAuthorizedToManage(userDetails, user);
		List<EntityModel<Borrowing>> list = user.getBorrowings().stream()
				.map(borrowing -> borrowingModelAssembler.toModel(borrowing)).collect(Collectors.toList());
		CollectionModel<EntityModel<Borrowing>> model = CollectionModel.of(list,
				linkTo(methodOn(UserManagementController.class).userBorrowings(null, username)).withRel("borrowings"));
		return ResponseEntity.ok(model);
	}

	@PostMapping("/users")
	public ResponseEntity addUser(@AuthenticationPrincipal UserDetails userDetails, @RequestBody LibraryUser user) {
		validateUser(user);
		checkAuthorizedToManage(userDetails, user);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		LibraryUser saved = userService.save(user);
		EntityModel<LibraryUser> model = userModelAssembler.toModel(saved);
		return ResponseEntity.created(
				linkTo(methodOn(UserManagementController.class).userByUsername(userDetails, saved.getUsername()))
						.toUri())
				.body(model);

	}

	@DeleteMapping("/users/{username}")
	public ResponseEntity deleteUser(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String username) {
		LibraryUser user = userService.findByUsername(username);
		checkAuthorizedToManage(userDetails, user);
		repository.delete(user);
		return ResponseEntity.noContent().build();

	}

	@PutMapping("/users/{username}")
	public ResponseEntity editUser(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String username,
			@RequestBody LibraryUser user) {
		LibraryUser oldUser = userService.findByUsername(username);
		LibraryUser currentUser = getCurrentUser(userDetails);
		if (!currentUser.canManageUser(user)) {
			throw new UnauthorizedUserException(oldUser.getRole());
		}
		overwriteUserData(user, oldUser);
		LibraryUser saved = userService.save(user);
		EntityModel<LibraryUser> model = userModelAssembler.toModel(saved);
		return ResponseEntity.created(
				linkTo(methodOn(UserManagementController.class).userByUsername(userDetails, saved.getUsername()))
						.toUri())
				.body(model);
	}

	private LibraryUser getCurrentUser(UserDetails userDetails) {
		LibraryUser user = userService.findByUsername(userDetails.getUsername());
		return user;
	}

	private void checkAuthorizedToManage(UserDetails userDetails, LibraryUser user) {
		LibraryUser currentUser = getCurrentUser(userDetails);
		if (!currentUser.canManageUser(user)) {
			throw new UnauthorizedUserException(user.getRole());
		}
	}

	private String getRole(UserDetails userDetails) {
		List<String> roles = userDetails.getAuthorities().stream().map(a -> ((GrantedAuthority) a).getAuthority())
				.collect(Collectors.toList());
		String role = roles.get(0);
		return role;
	}

	private void validateUser(LibraryUser user) {
		if (user.getUsername() == null || user.getUsername().isEmpty()) {
			throw new BadUserDetailsException("username");
		}
		if (user.getPassword() == null || user.getPassword().isEmpty()) {// check for length?
			throw new BadUserDetailsException("password");
		}
		if (user.getRole() == null) {
			throw new BadUserDetailsException("role");
		}
	}

	public void overwriteUserData(LibraryUser user, LibraryUser oldUser) {
		if (user.getUsername() != null && !user.getUsername().isEmpty()) {
			oldUser.setUsername(user.getUsername());
		}
		if (user.getPassword() != null && !user.getPassword().isEmpty()) {
			oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
		}
	}
}
