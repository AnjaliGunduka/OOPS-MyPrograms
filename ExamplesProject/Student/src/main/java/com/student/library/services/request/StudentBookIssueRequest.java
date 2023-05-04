package com.student.library.services.request;

import java.sql.Date;

public class StudentBookIssueRequest {
	private Long id;
	private Long bookid;
	private String bookName;
	private Date returnDate;
	private String requestedstatus;
	private int noOfBooks;

	public int getNoOfBooks() {
		return noOfBooks;
	}

	public void setNoOfBooks(int noOfBooks) {
		this.noOfBooks = noOfBooks;
	}

	public void setBookid(Long bookid) {
		this.bookid = bookid;
	}

	public String getRequestedstatus() {
		return requestedstatus;
	}

	public void setRequestedstatus(String requestedstatus) {
		this.requestedstatus = requestedstatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public Long getBookid() {
		// TODO Auto-generated method stub
		return bookid;
	}

}
