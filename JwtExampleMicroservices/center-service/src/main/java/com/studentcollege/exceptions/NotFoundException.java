package com.studentcollege.exceptions;

/**
 * 
 * 
 * 
 * @author muralikrishnak
 *
 */
public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NotFoundException(String message) {
		super(message);
	}

}
