package com.student.library.services.mapper;

import org.springframework.stereotype.Service;

import com.student.library.services.entity.Book;
import com.student.library.services.entity.RequestBook;
import com.student.library.services.entity.Student;
import com.student.library.services.enums.RequestStatus;
import com.student.library.services.request.RequestBookDto;



@Service
public class RequestMapper {
	
	/**
	 * This method is used to map RequestBook and RequestBookDto
	 * @param requestBookDto
	 * @param student
	 * @param book
	 * @return
	 */
	public RequestBook mapCreateStudentRequest(RequestBookDto requestBookDto,Student student,Book book)
	{
		return new RequestBook(student.getCardNo(),
				requestBookDto.getBookName(),RequestStatus.valueOf(requestBookDto.getStatus()),
				requestBookDto.getNoOfBooks(),requestBookDto.getRequestDate(), student,book);
	}
}
