package com.example.demo.Exception;

/**
 * 
 * 
 * 
 * @author Anjali Gunduka
 *
 */
public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NotFoundException(String message) {
		super(message);
	}

}
