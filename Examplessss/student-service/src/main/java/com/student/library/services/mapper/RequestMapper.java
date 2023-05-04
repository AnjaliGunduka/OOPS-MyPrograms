package com.student.library.services.mapper;

import org.springframework.stereotype.Service;

import com.student.library.services.entity.Book;
import com.student.library.services.entity.RequestBook;
import com.student.library.services.entity.Student;
import com.student.library.services.enums.RequestStatus;
import com.student.library.services.request.BookRequest;



@Service
public class RequestMapper {
	
	/**
	 * Maps Create bookRequest to book
	 * @param bookRequest
	 * @param student
	 * @param book
	 * @return
	 */
	public RequestBook mapCreateStudentRequest(BookRequest bookRequest,Student student,Book book)
	{
		return new RequestBook(student.getCardNo(),
				bookRequest.getBookName(),RequestStatus.valueOf(bookRequest.getStatus()),
				bookRequest.getNoOfBooks(),bookRequest.getRequestDate(), student,book);
	}
}
