package com.student.library.services.exception;

public class InactiveRequestBookException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InactiveRequestBookException(String message) {
		super(message);
	}
}
