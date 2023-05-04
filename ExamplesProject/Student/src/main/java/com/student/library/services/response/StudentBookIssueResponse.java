package com.student.library.services.response;

import java.sql.Date;

public class StudentBookIssueResponse {
	private String bookName;
	private String cardNo;
	private String requestedstatus;
	private Date issueDate;
	private Long studentID;
	private Date returnDate;

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
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

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
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

}
