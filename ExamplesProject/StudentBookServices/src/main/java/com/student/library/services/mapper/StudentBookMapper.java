package com.student.library.services.mapper;

import org.springframework.stereotype.Service;

import com.student.library.services.entity.Book;
import com.student.library.services.entity.RequestBook;
import com.student.library.services.entity.StudentBookIssue;
import com.student.library.services.request.StudentBookIssueRequest;

@Service
public class StudentBookMapper {
	public StudentBookIssue mapCreateStudentBookIssueRequest(StudentBookIssueRequest bookRequest, Book book,
			RequestBook requestBook) {
		return new StudentBookIssue(bookRequest.getReturnDate(), bookRequest.getNoOfBooks(),
				bookRequest.getRequestedstatus(), book, requestBook

		);
	}
}
