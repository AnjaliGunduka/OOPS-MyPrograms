package com.spring.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.demo.model.Book;
import com.spring.demo.repository.BookRepository;

@SpringBootApplication
@RestController
@RequestMapping("/book")
public class SpringMongodbDockerApplication {
	@Autowired
	private BookRepository repository;

	@PostMapping
	public Book saveBook(@RequestBody Book book) {
		return repository.save(book);
	}

	@GetMapping
	public List<Book> getBooks() {
		return repository.findAll();
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringMongodbDockerApplication.class, args);
	}

}
