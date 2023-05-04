package com.library.service.book.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.service.book.entity.Book;
import com.library.service.book.entity.RequestBook;
import com.library.service.book.entity.StudentBookIssue;
import com.library.service.book.mapper.StudentBookMapper;
import com.library.service.book.repository.StudentBookIssueRepository;
import com.library.service.book.request.StudentBookIssueRequest;
import com.speedment.jpastreamer.application.JPAStreamer;

@Service
public class StudentBookIssueService {
	@Autowired
	StudentBookMapper studentBookMapper;
	@Autowired
	StudentBookIssueRepository studentBookIssueRepository;
	@Autowired
	BookService bookService;
	@Autowired
	RequestBookService requestBookService;
	@Autowired
	private JPAStreamer jpaStreamer;

	public StudentBookIssue createRequestBook(StudentBookIssueRequest studentBookIssueRequest, Long bookId) {
		Book book = bookService.getBookById(bookId);
		RequestBook requestBook = requestBookService.getRequestBook(studentBookIssueRequest.getRequestid());
		StudentBookIssue studentBookIssue = studentBookMapper.mapCreateStudentBookIssueRequest(studentBookIssueRequest,
				book,requestBook);
		StudentBookIssue createdStudent = studentBookIssueRepository.save(studentBookIssue);
		return createdStudent;
	}
	
	
	public List<StudentBookIssue> getAllRequestedBooks() {
		return jpaStreamer.stream(StudentBookIssue.class).collect(Collectors.toList());
	}
}
