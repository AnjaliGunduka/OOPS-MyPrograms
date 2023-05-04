package com.student.library.services.request;



public class IssueBookRequest {
	private Long studentId;
	private Long BookId;
	
	
	
	public Long getStudentId() {
		return studentId;
	}
	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}
	public Long getBookId() {
		return BookId;
	}
	public void setBookId(Long bookId) {
		BookId = bookId;
	}
	
	
}
