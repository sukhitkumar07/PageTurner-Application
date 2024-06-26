package org.jsp.pageturner.controller;

import java.util.List;

import org.jsp.pageturner.dto.ResponseStructure;
import org.jsp.pageturner.model.Book;
import org.jsp.pageturner.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
@CrossOrigin
public class BookController {
	@Autowired
	private BookService bookService;

	@PostMapping("/{admin_id}")
	public ResponseEntity<ResponseStructure<Book>> saveBook(@RequestBody Book book, @PathVariable int admin_id) {
		return bookService.saveBook(book, admin_id);
	}

	@GetMapping
	public List<Book> findAll() {
		return bookService.findAll();
	}

	@GetMapping("/{admin_id}")
	public List<Book> findByAdminId(@PathVariable int admin_id) {
		return bookService.findByAdminId(admin_id);
	}

	@GetMapping("/find-by-name/{name}")
	public ResponseEntity<ResponseStructure<List<Book>>> findByName(@PathVariable String name) {
		return bookService.findByName(name);
	}

	@GetMapping("/find-by-category/{category}")
	public List<Book> findByCategory(@PathVariable String category) {
		return bookService.findByCategory(category);
	}

	@PutMapping
	public ResponseEntity<ResponseStructure<Book>> updateBook(@RequestBody Book book) {
		return bookService.updateBook(book);
	}

	@GetMapping("/find-by-id/{id}")
	public ResponseEntity<ResponseStructure<Book>> findById(@PathVariable int id) {
		return bookService.findById(id);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<String>> deleteById(@PathVariable int id) {
		return bookService.deleteById(id);
	}
	
	@PutMapping("/{user_id}/{book_id}")
	public ResponseEntity<ResponseStructure<String>> addToFavorite(int user_id,int book_id) {
		return bookService.addToFavorite(user_id, book_id);
	}
}
