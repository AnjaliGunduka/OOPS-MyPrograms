package com.student.library.services.response;

import java.time.Instant;

public class ProvideBookResponse {
	private Long id;
	private String bookName;
	private int noOfBooks;
	private String bookStatus;
	private String bookEdition;
	private String bookId;
	private Long requestBookId;
	private Long studentID;
	private String studentCardNo;
	private String studentUserName;
	private String studentBranch;
	private Instant requestDate;
	private String requestBookstatus;

	public String getRequestBookstatus() {
		return requestBookstatus;
	}

	public void setRequestBookstatus(String requestBookstatus) {
		this.requestBookstatus = requestBookstatus;
	}

	public Long getStudentID() {
		return studentID;
	}

	public void setStudentID(Long studentID) {
		this.studentID = studentID;
	}

	public Instant getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Instant requestDate) {
		this.requestDate = requestDate;
	}

	public String getBookEdition() {
		return bookEdition;
	}

	public void setBookEdition(String bookEdition) {
		this.bookEdition = bookEdition;
	}

	public String getStudentCardNo() {
		return studentCardNo;
	}

	public void setStudentCardNo(String studentCardNo) {
		this.studentCardNo = studentCardNo;
	}

	public String getStudentUserName() {
		return studentUserName;
	}

	public void setStudentUserName(String studentUserName) {
		this.studentUserName = studentUserName;
	}

	public String getStudentBranch() {
		return studentBranch;
	}

	public void setStudentBranch(String studentBranch) {
		this.studentBranch = studentBranch;
	}

	public Long getRequestBookId() {
		return requestBookId;
	}

	public void setRequestBookId(Long requestBookId) {
		this.requestBookId = requestBookId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public int getNoOfBooks() {
		return noOfBooks;
	}

	public void setNoOfBooks(int noOfBooks) {
		this.noOfBooks = noOfBooks;
	}

}
