package com.college.studentservices.exceptions;

public class InactiveCollegeException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public InactiveCollegeException(String message) {
		super(message);
	}
}
