package com.college.studentservices.response;



import com.college.studentservices.entities.Status;

public class StudentResponse {
	private Long id;
	private String studentNumber;
	private String type;
	private Status status;
	private String studentName;
	private String examcenterId;
	private String collegeCode;
	private String examCenter;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStudentNumber() {
		return studentNumber;
	}
	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getExamcenterId() {
		return examcenterId;
	}
	public void setExamcenterId(String examcenterId) {
		this.examcenterId = examcenterId;
	}
	public String getCollegeCode() {
		return collegeCode;
	}
	public void setCollegeCode(String collegeCode) {
		this.collegeCode = collegeCode;
	}
	public String getExamCenter() {
		return examCenter;
	}
	public void setExamCenter(String examCenter) {
		this.examCenter = examCenter;
	}
}
