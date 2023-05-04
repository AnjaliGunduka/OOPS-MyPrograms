package com.books_service.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.books_service.Entity.Book;
import com.books_service.Entity.BookResponse;
import com.books_service.Repository.BookRepository;
import com.books_service.services.BookService;

import io.swagger.annotations.Api;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Spliterator;

@RestController
@RequestMapping(value = "/bookServicess/v1")
@Api(value = "BOOK", tags = { "book Management" })
@Validated
public class BookController {

    @Autowired
    private BookService bookservice;
    @Autowired
    BookRepository bookRepository;
    @GetMapping("/books")
    public List<BookResponse> getAllBooks() {
        return bookservice.getAllBooks();
    }

    

    @PostMapping("/books")
    public void addBook(@RequestBody Book book) {
        bookservice.addBook(book);
    }
    
    
    @GetMapping("/books/{id}")
    public Book getBook(@PathVariable String id) {
        return bookservice.getBook(id);
    }
    
    @RequestMapping(value = "/count", method = RequestMethod.GET, produces = "application/json")
	public List<Book> getAllBookss() {
		return bookservice.getAllBookss();
	}
    
    
//    @GetMapping("/books/{title}")
//	public Iterable<Book> getTitles(@PathVariable String title) {
//		return bookRepository.findByTitle(title);
//	}
    @PutMapping("/books/{id}")
    public void updateBook(@PathVariable String id, @RequestBody Book book) {
        bookservice.updateBook(id, book);
    }

    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable String id) {
        bookservice.deleteBook(id);
    }
  
   
}
