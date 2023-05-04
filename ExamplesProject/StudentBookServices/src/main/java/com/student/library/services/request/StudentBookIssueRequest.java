package com.student.library.services.request;

import java.sql.Date;

import javax.persistence.Column;

import com.student.library.services.enums.RequestedStudent;

public class StudentBookIssueRequest {
	private Long id;
	private Date returnDate;
	private int noOfBooks;
	private RequestedStudent requestedstatus;
	private String bookName;
	
	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public RequestedStudent getRequestedstatus() {
		return requestedstatus;
	}

	public void setRequestedstatus(RequestedStudent requestedstatus) {
		this.requestedstatus = requestedstatus;
	}

	public int getNoOfBooks() {
		return noOfBooks;
	}

	public void setNoOfBooks(int noOfBooks) {
		this.noOfBooks = noOfBooks;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

}
