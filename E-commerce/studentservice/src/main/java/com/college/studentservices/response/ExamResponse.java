package com.college.studentservices.response;

public class ExamResponse {
	private Long id;
	private String CollegeName;
	private String examCenter;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCollegeName() {
		return CollegeName;
	}
	public void setCollegeName(String collegeName) {
		CollegeName = collegeName;
	}
	public String getExamCenter() {
		return examCenter;
	}
	public void setExamCenter(String examCenter) {
		this.examCenter = examCenter;
	}
}
