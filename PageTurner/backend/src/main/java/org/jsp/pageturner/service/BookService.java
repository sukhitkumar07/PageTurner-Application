package org.jsp.pageturner.service;

import java.util.List;
import java.util.Optional;

import org.jsp.pageturner.dao.AdminDao;
import org.jsp.pageturner.dao.BookDao;
import org.jsp.pageturner.dao.UserDao;
import org.jsp.pageturner.dto.ResponseStructure;
import org.jsp.pageturner.exception.BookNotFoundException;
import org.jsp.pageturner.model.Admin;
import org.jsp.pageturner.model.Book;
import org.jsp.pageturner.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BookService {
	@Autowired
	private BookDao bookDao;
	@Autowired
	private AdminDao adminDao;
	@Autowired
	private UserDao userDao;

	public ResponseEntity<ResponseStructure<Book>> saveBook(Book book, int admin_id) {
		ResponseStructure<Book> structure = new ResponseStructure<>();
		Optional<Admin> recAdmin = adminDao.findById(admin_id);
		if (recAdmin.isPresent()) {
			recAdmin.get().getBooks().add(book);
			book.setAdmin(recAdmin.get());
			structure.setMessage("Book saved");
			structure.setBody(bookDao.saveBook(book));
			structure.setStatusCode(HttpStatus.CREATED.value());
			return new ResponseEntity<ResponseStructure<Book>>(structure, HttpStatus.CREATED);
		} else {
			throw new BookNotFoundException("Invalid Admin Id");
		}

	}

	public List<Book> findAll() {
		return bookDao.findAll();
	}

	public List<Book> findByAdminId(int admin_id) {
		return bookDao.findByAdminId(admin_id);
	}

	public ResponseEntity<ResponseStructure<Book>> updateBook(Book book) {
		ResponseStructure<Book> structure = new ResponseStructure<>();
		Optional<Book> recBook = bookDao.findById(book.getId());
		if (recBook.isPresent()) {
			Book dbBook = recBook.get();
			dbBook.setName(book.getName());
			dbBook.setAuthor(book.getAuthor());
			dbBook.setCategory(book.getCategory());
			dbBook.setDescription(book.getDescription());
			dbBook.setImg_url(book.getImg_url());
			dbBook.setUrl(book.getUrl());
			dbBook.setIsbn(book.getIsbn());
			dbBook.setPrice(book.getPrice());
			structure.setMessage("Book Updated");
			structure.setBody(bookDao.saveBook(dbBook));
			structure.setStatusCode(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<ResponseStructure<Book>>(structure, HttpStatus.ACCEPTED);
		}
		throw new BookNotFoundException("Id is invalid");
	}

	public List<Book> findByCategory(String category) {
		List<Book> books = bookDao.findByCategory(category);
		if (!books.isEmpty()) {
			return books;
		}
		throw new BookNotFoundException("Invalid Category");

	}

	public ResponseEntity<ResponseStructure<List<Book>>> findByName(String name) {
		List<Book> recBooks = bookDao.findByName(name);
		ResponseStructure<List<Book>> structure = new ResponseStructure<>();
		if (!recBooks.isEmpty()) {
			structure.setMessage("Book Found");
			structure.setBody(recBooks);
			structure.setStatusCode(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<ResponseStructure<List<Book>>>(structure, HttpStatus.ACCEPTED);
		}
		throw new BookNotFoundException("Invalid Book Name");
	}

	public ResponseEntity<ResponseStructure<Book>> findById(int id) {
		Optional<Book> recBook = bookDao.findById(id);
		ResponseStructure<Book> structure = new ResponseStructure<>();
		if (recBook.isPresent()) {
			structure.setMessage("Book found");
			structure.setBody(recBook.get());
			structure.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<ResponseStructure<Book>>(structure, HttpStatus.OK);
		}
		throw new BookNotFoundException("Invalid Book Id");
	}

	public ResponseEntity<ResponseStructure<String>> deleteById(int id) {
		ResponseStructure<String> structure = new ResponseStructure<>();
		if (bookDao.deleteByid(id)) {
			structure.setMessage("Book found");
			structure.setBody("Book Deleted");
			structure.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<ResponseStructure<String>>(structure, HttpStatus.OK);
		}
		throw new BookNotFoundException("Invalid Book Id");
	}

	public ResponseEntity<ResponseStructure<String>> addToFavorite(int user_id, int book_id) {
		Optional<Book> recBook = bookDao.findById(book_id);
		Optional<User> recUser = userDao.findById(user_id);
		if (recBook.isEmpty() || recUser.isEmpty())
			throw new IllegalArgumentException("Invalid Product ID or User ID");

		recUser.get().getFavorites().add(recBook.get());
		userDao.saveUser(recUser.get());
		ResponseStructure<String> structure = new ResponseStructure<>();
		structure.setBody("User and Product Found");
		structure.setMessage("Added Product to cart");
		structure.setStatusCode(HttpStatus.ACCEPTED.value());
		return new ResponseEntity<ResponseStructure<String>>(structure, HttpStatus.ACCEPTED);
	}
}
