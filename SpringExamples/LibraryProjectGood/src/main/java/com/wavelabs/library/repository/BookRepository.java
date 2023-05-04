package com.wavelabs.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wavelabs.library.model.Book;
import com.wavelabs.library.model.Category;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	public Book findByTag(String tag);
	public List<Book> findByCategory(Category category);
	public List<Book> findByCategoryAndStatus(Category category, Integer status);
	public Long countByStatus(Integer status);
}
