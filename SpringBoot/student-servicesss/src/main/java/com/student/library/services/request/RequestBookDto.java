package com.student.library.services.request;

import java.time.Instant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RequestBookDto {
	@NotBlank(message = "Book Name should not be null or Empty")
	private String bookName;
	private String status;
	private int noOfBooks;
	private Instant requestDate;
	@NotNull(message = "Student Card Number should not be null")
	private String cardNo;

	public RequestBookDto(@NotBlank(message = "Book Name should not be null or Empty") String bookName, String status,
			int noOfBooks, Instant requestDate,
			@NotNull(message = "Student Card Number should not be null") String cardNo) {
		super();
		this.bookName = bookName;
		this.status = status;
		this.noOfBooks = noOfBooks;
		this.requestDate = requestDate;
		this.cardNo = cardNo;
	}

	public Instant getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Instant requestDate) {
		this.requestDate = requestDate;
	}

	public RequestBookDto() {
		super();
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
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
	@Override
	public String toString() {
		return "RequestBookDto [bookName=" + bookName + ", status=" + status + ", noOfBooks=" + noOfBooks
				+ ", requestDate=" + requestDate + ", cardNo=" + cardNo + "]";
	}
}
