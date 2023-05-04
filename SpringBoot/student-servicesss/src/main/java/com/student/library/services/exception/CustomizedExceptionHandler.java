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

import com.google.gson.Gson;

import feign.FeignException;

@ControllerAdvice
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public final ResponseEntity<ErrorResponse> handleNotFoundException(WebRequest request, NotFoundException ex) {
		return sendResponse(request, ex, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(RequestStatusException.class)
	public final ResponseEntity<ErrorResponse> handleBookNotStatusException(WebRequest request,
			RequestStatusException ex) {
		return sendResponse(request, ex, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InsufficientNoOfCopiesException.class)
	public final ResponseEntity<ErrorResponse> handleInsufficientBalanceException(WebRequest request,
			InsufficientNoOfCopiesException ex) {
		return sendResponse(request, ex, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(BookNotProvidedException.class)
	public final ResponseEntity<ErrorResponse> handleInvalidBookException(WebRequest request,
			BookNotProvidedException ex) {
		return sendResponse(request, ex, HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity<ErrorResponse> sendResponse(WebRequest request, Exception ex, HttpStatus httpStatus) {
		List<String> errors = new ArrayList<>();
		errors.add(ex.getMessage());
		ErrorResponse errorRecord = new ErrorResponse(Constants.FAILED, errors, httpStatus.value(),
				Instant.now().toString(), request.getDescription(false), httpStatus);
		return ResponseEntity.status(httpStatus).body(errorRecord);
	}

	@ExceptionHandler(FeignException.class)
	public final ResponseEntity<ErrorResponse> handleFeignException(WebRequest request, FeignException feignException) {
		List<String> errors = new ArrayList<>();
		Gson gson = new Gson();
		ErrorResponse errorResponse = null;
		if (feignException.status() == -1) {
			errors.add("Book Service is Unavailable. Unable to Connect! Please try after sometime.");
			errorResponse = new ErrorResponse(Constants.FAILED, errors, HttpStatus.SERVICE_UNAVAILABLE.value(),
					Instant.now().toString(), request.getDescription(false), HttpStatus.SERVICE_UNAVAILABLE);
		} else {
			errorResponse = gson.fromJson(feignException.contentUTF8(), ErrorResponse.class);
			errorResponse.setPath(request.getDescription(false));
			errorResponse.setTimestamp(Instant.now().toString());
		}
		return ResponseEntity.status(errorResponse.getHttpStatus()).body(errorResponse);
	}
}
