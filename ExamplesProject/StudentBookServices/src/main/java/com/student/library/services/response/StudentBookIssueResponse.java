package com.student.library.services.response;

import java.sql.Date;

public class StudentBookIssueResponse {
	private String bookName;
	private String requestedstatus;
	private Date issueDate;
	private int noOfBooks;
	private Long studentID;
	private String bookEdition;
	private String bookAuthor;
	private String bookPublisher;
	private String bookStatus;
	private String bookId;
	private String requestBookCardNo;
	private String requestBookBranch;
	private Date returnDate;

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public String getRequestBookCardNo() {
		return requestBookCardNo;
	}

	public void setRequestBookCardNo(String requestBookCardNo) {
		this.requestBookCardNo = requestBookCardNo;
	}

	public String getRequestBookBranch() {
		return requestBookBranch;
	}

	public void setRequestBookBranch(String requestBookBranch) {
		this.requestBookBranch = requestBookBranch;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getBookStatus() {
		return bookStatus;
	}

	public void setBookStatus(String bookStatus) {
		this.bookStatus = bookStatus;
	}

	public String getBookEdition() {
		return bookEdition;
	}

	public void setBookEdition(String bookEdition) {
		this.bookEdition = bookEdition;
	}

	public String getBookAuthor() {
		return bookAuthor;
	}

	public void setBookAuthor(String bookAuthor) {
		this.bookAuthor = bookAuthor;
	}

	public String getBookPublisher() {
		return bookPublisher;
	}

	public void setBookPublisher(String bookPublisher) {
		this.bookPublisher = bookPublisher;
	}

	public Long getStudentID() {
		return studentID;
	}

	public void setStudentID(Long studentID) {
		this.studentID = studentID;
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
