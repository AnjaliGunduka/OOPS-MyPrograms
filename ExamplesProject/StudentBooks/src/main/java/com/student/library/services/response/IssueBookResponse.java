package com.student.library.services.response;

import java.sql.Date;

public class IssueBookResponse {
	private Long id;
	private String bookName;
	private String studentuserName;
	private int NoOfBooks;
	private Date issueDate;
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
	public String getStudentuserName() {
		return studentuserName;
	}
	public void setStudentuserName(String studentuserName) {
		this.studentuserName = studentuserName;
	}
	public int getNoOfBooks() {
		return NoOfBooks;
	}
	public void setNoOfBooks(int noOfBooks) {
		NoOfBooks = noOfBooks;
	}
	public Date getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}
	
}
