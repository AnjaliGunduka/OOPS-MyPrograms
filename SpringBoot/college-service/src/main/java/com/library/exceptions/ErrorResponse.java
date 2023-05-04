package com.library.exceptions;

import java.util.List;

import org.springframework.http.HttpStatus;

/**
 * 
 * 
 * 
 * @author muralikrishnak
 *
 */
public class ErrorResponse {

	private String error;
	private List<String> errorMessages;
	private int status;
	private String timestamp;
	private String path;
	private HttpStatus httpStatus;

	public ErrorResponse() {

	}

	public ErrorResponse(String error, List<String> errorMessages, int status, String timestamp, String path,
			HttpStatus httpStatus) {
		this.errorMessages = errorMessages;
		this.error = error;
		this.status = status;
		this.timestamp = timestamp;
		this.path = path;
		this.httpStatus = httpStatus;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public List<String> getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

}
