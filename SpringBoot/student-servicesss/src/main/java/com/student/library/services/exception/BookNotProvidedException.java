package com.student.library.services.exception;

public class BookNotProvidedException extends Exception{
	private static final long serialVersionUID = 1L;

	public BookNotProvidedException(String message) {
		super(message);
	}
}
