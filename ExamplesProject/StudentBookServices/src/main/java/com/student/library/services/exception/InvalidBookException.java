package com.student.library.services.exception;

public class InvalidBookException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidBookException(String message) {
		super(message);
	}

}
