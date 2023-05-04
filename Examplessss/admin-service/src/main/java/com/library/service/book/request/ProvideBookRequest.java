package com.library.service.book.request;


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
	@NotNull(message = "Status should not be null or empty")
	private RequestStatus status;

	public ProvideBookRequest(Long bookId, Long requestBookId, int noOfBooks, RequestStatus status) {
		super();
		this.bookId = bookId;
		this.requestBookId = requestBookId;
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
