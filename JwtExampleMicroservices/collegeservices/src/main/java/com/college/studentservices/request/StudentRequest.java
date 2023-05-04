package com.college.studentservices.request;

import javax.validation.constraints.NotBlank;

import com.college.studentservices.entities.Status;
import com.college.studentservices.validators.EnumValidator;


public class StudentRequest {
	@NotBlank(message = "Student Type should not be null or Empty")
	private String type;
	@NotBlank(message = "Student Status should not be null or Empty")
	@EnumValidator(message = "Invalid Student Status", enumType = Status.class)
	private String status;
	@NotBlank(message = "Student Name should not be null or Empty")
	private String studentName;
	@NotBlank(message = "Center Id should not be null or Empty")
	private String examcenterId;
	@NotBlank(message = "College Category Code should not be null or Empty")
	private String collegeCode;
	private String examCenter;
	public String getExamCenter() {
		return examCenter;
	}
	public void setExamCenter(String examCenter) {
		this.examCenter = examCenter;
	}
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
	
}
