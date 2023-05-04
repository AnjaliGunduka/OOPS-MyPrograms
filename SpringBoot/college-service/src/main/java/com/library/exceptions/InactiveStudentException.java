package com.library.exceptions;

public class InactiveStudentException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public InactiveStudentException(String message) {
		super(message);
	}
}
