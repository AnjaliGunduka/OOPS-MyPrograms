package com.library.service.book.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.service.book.entity.Book;
import com.library.service.book.entity.RequestBook;
import com.library.service.book.entity.StudentBookIssue;



public interface StudentBookIssueRepository extends JpaRepository<StudentBookIssue, Long> {

	Optional<StudentBookIssue> findByIdAndBookAndRequestBook(Long issueId,RequestBook requestBook, Book book);

}
