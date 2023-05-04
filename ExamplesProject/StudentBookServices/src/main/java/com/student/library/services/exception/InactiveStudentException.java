package com.student.library.services.exception;

public class InactiveStudentException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public InactiveStudentException(String message) {
		super(message);
	}
}
