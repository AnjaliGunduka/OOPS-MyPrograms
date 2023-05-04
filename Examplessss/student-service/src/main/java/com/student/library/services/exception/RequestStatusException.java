package com.student.library.services.exception;

public class RequestStatusException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RequestStatusException(String message) {
		super(message);
	}
}
