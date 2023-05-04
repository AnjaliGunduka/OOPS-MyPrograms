package com.student.library.services.request;

import java.time.Instant;
 

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.student.library.services.enums.RequestStatus;

public class ProvideBookRequest {

	@Min(value = 1, message = "Invalid Book Id")
	@NotNull(message = "Book Id should not be null")
	private Long bookId;
	@Min(value = 1, message = "Invalid RequestBook Id")
	@NotNull(message = "Request Book Id should not be null")
	private Long requestBookId;
	private Instant requestDate;
	private int noOfBooks;

	@NotNull(message = "Status should not be null")
	private RequestStatus status;

	public Instant getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Instant requestDate) {
		this.requestDate = requestDate;
	}

	public ProvideBookRequest(
			@Min(value = 1, message = "Invalid Book Id") @NotNull(message = "Book Id should not be null") Long bookId,
			@Min(value = 1, message = "Invalid RequestBook Id") @NotNull(message = "Request Book Id should not be null") Long requestBookId,
			Instant requestDate, int noOfBooks, @NotNull(message = "Status should not be null") RequestStatus status) {
		super();
		this.bookId = bookId;
		this.requestBookId = requestBookId;
		this.requestDate = requestDate;
		this.noOfBooks = noOfBooks;
		this.status = status;
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
