package spring.demo.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import spring.demo.model.Book;

public interface BookRepository extends MongoRepository<Book, Integer> {
}
