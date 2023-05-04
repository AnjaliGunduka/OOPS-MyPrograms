package com.library.service.book.exception;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(AccessDeniedException.class)
	public void handleAccessDeniedException(HttpServletResponse res) throws IOException {
		res.sendError(HttpStatus.UNAUTHORIZED.value());
	}

	@ExceptionHandler(NotFoundException.class)
	public final ResponseEntity<ErrorResponse> handleNotFoundException(WebRequest request, NotFoundException ex) {
		return sendResponse(request, ex, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(NoOfCopiesNotAvailableException.class)
	public final ResponseEntity<ErrorResponse> handleInsufficientBalanceException(WebRequest request,
			NoOfCopiesNotAvailableException ex) {
		return sendResponse(request, ex, HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity<ErrorResponse> sendResponse(WebRequest request, Exception ex, HttpStatus httpStatus) {
		List<String> errors = new ArrayList<>();
		errors.add(ex.getMessage());
		ErrorResponse errorRecord = new ErrorResponse(Constants.FAILED, errors, httpStatus.value(),
				Instant.now().toString(), request.getDescription(false), httpStatus);
		return ResponseEntity.status(httpStatus).body(errorRecord);
	}

}
