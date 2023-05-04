package com.student.library.services.exception;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;





@ControllerAdvice
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler {

	
	

	private ResponseEntity<ErrorResponse> sendResponse(WebRequest request, Exception ex, HttpStatus httpStatus) {
		List<String> errors = new ArrayList<>();
		errors.add(ex.getMessage());
		ErrorResponse errorRecord = new ErrorResponse(Constants.FAILED, errors, httpStatus.value(),
				Instant.now().toString(), request.getDescription(false), httpStatus);
		return ResponseEntity.status(httpStatus).body(errorRecord);
	}

	@ExceptionHandler(NotFoundException.class)
	public final ResponseEntity<ErrorResponse> handleNotFoundException(WebRequest request, NotFoundException ex) {
		return sendResponse(request, ex, HttpStatus.NOT_FOUND);
	}
	

	@ExceptionHandler(InsufficientNoOfCopiesException.class)
	public final ResponseEntity<ErrorResponse> handleInsufficientBalanceException(WebRequest request,
			InsufficientNoOfCopiesException ex) {
		return sendResponse(request, ex, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidBookException.class)
	public final ResponseEntity<ErrorResponse> handleInvalidBookException(WebRequest request,
			InvalidBookException ex) {
		return sendResponse(request, ex, HttpStatus.BAD_REQUEST);
	}


}
