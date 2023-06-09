package com.college.studentservices.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ExamRequest {
	@Min(value = 1, message = "Invalid College Id")
	@NotNull(message = "College Id should not be null")
	private Long collegeId;
	@Min(value = 1, message = "Invalid Student Id")
	@NotNull(message = "Student Id should not be null")
	private Long studentId;
	
	public Long getCollegeId() {
		return collegeId;
	}
	public void setCollegeId(Long collegeId) {
		this.collegeId = collegeId;
	}
	public Long getStudentId() {
		return studentId;
	}
	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}
	
}
