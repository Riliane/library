package com.example.library.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.library.controllers.errorhandling.BadSortingException;
import com.example.library.controllers.errorhandling.UnauthorizedUserException;
import com.example.library.models.Book;
import com.example.library.models.BookOrderColumns;
import com.example.library.models.Borrowing;
import com.example.library.models.Genre;
import com.example.library.models.LibraryUser;
import com.example.library.models.SortingOrder;
import com.example.library.security.UserService;
import com.example.library.service.BookService;
import com.example.library.service.BorrowingService;

@RestController
public class BookController {
	@Autowired
	private BookService bookService;
	@Autowired
	private UserService userService;
	@Autowired
	private BorrowingService borrowingService;
	@Autowired
	private BookModelAssembler bookModelAssembler;
	@Autowired
	private BorrowingModelAssembler borrowingModelAssembler;

	@GetMapping("/books")
	public ResponseEntity findBooks(@RequestParam(required = false) String name,
			@RequestParam(required = false) Genre genre, @RequestParam(required = false) String author,
			@RequestParam(required = false) BookOrderColumns orderBy,
			@RequestParam(required = false) SortingOrder order) {
		if ((orderBy != null && order == null) || (orderBy == null && order != null)) {
			throw new BadSortingException();
		}
//		Genre genreObj = null;
//		if (genre != null) {
//			genreObj = Genre.valueOf(genre);
//		}

		List<Book> books = bookService.findBooks(name, genre, author, orderBy, order);
		CollectionModel<EntityModel<Book>> model = CollectionModel.of(
				books.stream().map(book -> bookModelAssembler.toModel(book)).collect(Collectors.toList()),
				linkTo(methodOn(BookController.class).findBooks(name, genre, author, orderBy, order)).withSelfRel());
		return ResponseEntity.ok(model);
	}

	@PostMapping("/books")
	public ResponseEntity addBook(@RequestBody Book book) {
		Book newBook = bookService.addBook(book);
		return ResponseEntity.created(linkTo(methodOn(BookController.class).bookById(newBook.getBookId())).toUri())
				.body(bookModelAssembler.toModel(newBook));
	}

	@GetMapping("/books/{id}")
	public ResponseEntity bookById(@PathVariable Long id) {
		Book book = bookService.findById(id);

		return ResponseEntity.ok(bookModelAssembler.toModel(book));
	}

	@DeleteMapping("/books/{id}")
	public ResponseEntity deleteBook(@PathVariable Long id, @RequestParam(required = false) Integer quantity) {
		bookService.deleteBook(id, quantity);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/books/{id}")
	public ResponseEntity replaceBook(@PathVariable Long id, @RequestBody Book book) {
		Book newBook = bookService.replaceBook(id, book);
		return ResponseEntity.created(linkTo(methodOn(BookController.class).bookById(newBook.getBookId())).toUri())
				.body(bookModelAssembler.toModel(newBook));
	}

	@PostMapping("/books/{id}/borrow")
	public ResponseEntity borrowBook(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
		LibraryUser user = userService.findByUsername(userDetails.getUsername());
		Book book = bookService.findById(id);
		return ResponseEntity.ok(borrowingModelAssembler.toModel(borrowingService.borrowBook(book, user)));
	}

	@PostMapping("/books/{id}/return")
	public ResponseEntity returnBook(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
		LibraryUser user = userService.findByUsername(userDetails.getUsername());
		Book book = bookService.findById(id);
		return ResponseEntity.ok(borrowingModelAssembler.toModel(borrowingService.returnBook(user, book)));
	}

	@PostMapping("/books/borrowing/{id}/return")
	public ResponseEntity returnByBorrowing(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
		Borrowing borrowing = borrowingService.findById(id);
		if (!borrowing.getReader().getUsername().equals(userDetails.getUsername())) {
			throw new UnauthorizedUserException();
		}
		return ResponseEntity.ok(borrowingModelAssembler.toModel(borrowingService.returnByBorrowing(borrowing)));
	}
}
