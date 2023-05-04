package com.student.library.services.request;

import java.time.Instant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class BookRequest {
	@NotBlank(message = "Book Name should not be null or Empty")
	private String bookName;
	@NotBlank(message = "Status should not be null or Empty")
	private String status;
	@NotNull(message = "No of Books should not be null")
	private int noOfBooks;
	private Instant requestDate;
	@NotNull(message = "Student Card Number should not be null")
	private String cardNo;

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Instant getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Instant requestDate) {
		this.requestDate = requestDate;
	}

	public int getNoOfBooks() {
		return noOfBooks;
	}

	public void setNoOfBooks(int noOfBooks) {
		this.noOfBooks = noOfBooks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

}
