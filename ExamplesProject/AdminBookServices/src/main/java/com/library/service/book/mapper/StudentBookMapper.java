package com.library.service.book.mapper;

import org.springframework.stereotype.Service;

import com.library.service.book.entity.Book;
import com.library.service.book.entity.RequestBook;
import com.library.service.book.entity.StudentBookIssue;
import com.library.service.book.enums.RequestedStudent;
import com.library.service.book.request.StudentBookIssueRequest;


@Service
public class StudentBookMapper {
	public StudentBookIssue mapCreateStudentBookIssueRequest(StudentBookIssueRequest bookRequest, Book book,RequestBook requestBook) {
		return new StudentBookIssue(bookRequest.getIssueDate(), bookRequest.getNoOfBooks(),
				RequestedStudent.valueOf(bookRequest.getRequestedstatus()), book,requestBook

		);
	}
}
