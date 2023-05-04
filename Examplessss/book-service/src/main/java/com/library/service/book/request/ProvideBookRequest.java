package com.library.service.book.request;


import java.time.Instant;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.library.service.book.enums.RequestStatus;

public class ProvideBookRequest {

	@Min(value = 1, message = "Invalid Book Id")
	@NotNull(message = "Book Id should not be null or empty")
	private Long bookId;
	@Min(value = 1, message = "Invalid RequestBook Id")
	@NotNull(message = "Request Book Id should not be null or empty")
	private Long requestBookId;
	@NotNull(message = "No of Books should not be null or empty")
	private int noOfBooks;
	private Instant date;
	@NotNull(message = "Status should not be null or empty")
	private RequestStatus status;

	

	public ProvideBookRequest(
			@Min(value = 1, message = "Invalid Book Id") @NotNull(message = "Book Id should not be null or empty") Long bookId,
			@Min(value = 1, message = "Invalid RequestBook Id") @NotNull(message = "Request Book Id should not be null or empty") Long requestBookId,
			@NotNull(message = "No of Books should not be null or empty") int noOfBooks, Instant date,
			@NotNull(message = "Status should not be null or empty") RequestStatus status) {
		super();
		this.bookId = bookId;
		this.requestBookId = requestBookId;
		this.noOfBooks = noOfBooks;
		this.date = date;
		this.status = status;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	public ProvideBookRequest() {
		super();
	}

	public RequestStatus getStatus() {
		return status;
	}

	public void setStatus(RequestStatus status) {
		this.status = status;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public Long getRequestBookId() {
		return requestBookId;
	}

	public void setRequestBookId(Long requestBookId) {
		this.requestBookId = requestBookId;
	}

	public int getNoOfBooks() {
		return noOfBooks;
	}

	public void setNoOfBooks(int noOfBooks) {
		this.noOfBooks = noOfBooks;
	}

}
