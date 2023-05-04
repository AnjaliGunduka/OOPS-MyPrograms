package com.spring.demo.repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.spring.demo.model.Book;
public interface BookRepository  extends MongoRepository<Book,Integer>{

}
