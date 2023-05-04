package com.student.library.services.mapper;

import org.springframework.stereotype.Service;

import com.student.library.services.entity.RequestBook;
import com.student.library.services.entity.Student;
import com.student.library.services.enums.RequestStatus;
import com.student.library.services.request.BookRequest;



@Service
public class RequestMapper {
	public RequestBook mapCreateStudentRequest(BookRequest bookRequest,Student student)
	{
		return new RequestBook(student.getCardNo(),
				bookRequest.getBookName(),RequestStatus.valueOf(bookRequest.getStatus()),
				bookRequest.getNoOfBooks(),student);
	}
}
