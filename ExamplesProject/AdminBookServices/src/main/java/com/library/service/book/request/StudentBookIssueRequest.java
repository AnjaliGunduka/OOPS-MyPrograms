package com.library.service.book.request;

import java.sql.Date;

public class StudentBookIssueRequest {

	private String requestedstatus;
	private Date issueDate;
	private int noOfBooks;
	private String bookName;
	private Long requestid;
	

	public Long getRequestid() {
		return requestid;
	}

	public void setRequestid(Long requestid) {
		this.requestid = requestid;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getRequestedstatus() {
		return requestedstatus;
	}

	public void setRequestedstatus(String requestedstatus) {
		this.requestedstatus = requestedstatus;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public int getNoOfBooks() {
		return noOfBooks;
	}

	public void setNoOfBooks(int noOfBooks) {
		this.noOfBooks = noOfBooks;
	}

}
