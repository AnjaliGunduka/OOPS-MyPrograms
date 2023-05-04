package com.college.studentservices.exceptions;

public class InsufficientexamCenterException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public InsufficientexamCenterException(String message) {
		super(message);
	}
	
}
