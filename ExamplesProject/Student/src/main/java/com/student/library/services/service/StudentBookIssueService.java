package com.student.library.services.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.student.library.services.entity.Book;
import com.student.library.services.entity.StudentBookIssue;
import com.student.library.services.enums.RequestedStudent;
import com.student.library.services.exception.Constants;
import com.student.library.services.exception.InvalidBookException;
import com.student.library.services.repository.BookRepository;
import com.student.library.services.repository.StudentBookIssueRepository;
import com.student.library.services.request.StudentBookIssueRequest;


@Service
public class StudentBookIssueService {
	private static final Logger log = LoggerFactory.getLogger(StudentBookIssueService.class);
	@Autowired
	StudentBookIssueRepository studentBookIssueRepository;
	@Autowired
	BookService bookservice;
	@Autowired
	private JPAStreamer jpaStreamer;
	@Autowired
	private BookRepository bookRepository;
	/**
	 * 
	 * @param studentBookIssueRequest
	 * @return
	 * @throws InvalidBookException
	 */

	public StudentBookIssue createStudentBook(StudentBookIssueRequest studentBookIssueRequest)
			throws InvalidBookException {
		ModelMapper modelMapper = new ModelMapper();
		Book book = bookservice.getBookById(studentBookIssueRequest.getBookid());
		book.setNumberOfCopies(book.getNumberOfCopies()+studentBookIssueRequest.getNoOfBooks());
		StudentBookIssue studentBookIssue = modelMapper.map(studentBookIssueRequest, StudentBookIssue.class);
		checkBookApproved(studentBookIssue);
		bookRepository.save(book);
		StudentBookIssue createdStudentBookIssue = studentBookIssueRepository.save(studentBookIssue);
		log.info("StudentBookIssue  created");
		return createdStudentBookIssue;
	}
	
	
	/**
	 * 
	 * @param studentBookIssue
	 * @throws InvalidBookException
	 */
	private void checkBookApproved(StudentBookIssue studentBookIssue) throws InvalidBookException  {
		if (studentBookIssue.getRequestedstatus() == RequestedStudent.REJECTED) {
			throw new InvalidBookException(Constants.INVALID_BOOK_REJECTED);
		}
	}
	
	/**
	 * List all Issued Books
	 * @return
	 */
	
	
	public List<StudentBookIssue> getAllIssuedBooks() {
		return jpaStreamer.stream(StudentBookIssue.class).collect(Collectors.toList());
	}
	
	
	
}
