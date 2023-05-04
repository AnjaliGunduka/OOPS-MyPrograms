package com.student.library.services.request;


public class BookRequest {
	private Long bookid;
	private String bookName;
	private String studentCardNo;
	private String status;
	
	
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Long getBookid() {
		return bookid;
	}
	public void setBookid(Long bookid) {
		this.bookid = bookid;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	
	public String getStudentCardNo() {
		return studentCardNo;
	}
	public void setStudentCardNo(String studentCardNo) {
		this.studentCardNo = studentCardNo;
	}

	
}
