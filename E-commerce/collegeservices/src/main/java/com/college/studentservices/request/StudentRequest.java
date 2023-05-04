package com.college.studentservices.request;

import javax.validation.constraints.NotBlank;

import com.college.studentservices.entities.Status;


public class StudentRequest {
	@NotBlank(message = "Student Type should not be null or Empty")
	private String type;
	@NotBlank(message = "Student Status should not be null or Empty")
	private String status;
	@NotBlank(message = "Student Name should not be null or Empty")
	private String studentName;
	@NotBlank(message = "College Category Code should not be null or Empty")
	private String collegeCode;
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	
	public String getCollegeCode() {
		return collegeCode;
	}
	public void setCollegeCode(String collegeCode) {
		this.collegeCode = collegeCode;
	}
	
}
