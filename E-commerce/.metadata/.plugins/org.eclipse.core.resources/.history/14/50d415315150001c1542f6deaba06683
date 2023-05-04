package com.studentcollege.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CenterRequest {
	@Min(value = 1, message = "Invalid College Id")
	@NotNull(message = "College Id should not be null")
	private Long collegeId;
	@Min(value = 1, message = "Invalid Student Id")
	@NotNull(message = "Student Id should not be null")
	private Long studentId;
	@NotNull(message = "Exam Center should not be null")
	private String examCenterName;
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
	public String getExamCenterName() {
		return examCenterName;
	}
	public void setExamCenterName(String examCenterName) {
		this.examCenterName = examCenterName;
	}
}
