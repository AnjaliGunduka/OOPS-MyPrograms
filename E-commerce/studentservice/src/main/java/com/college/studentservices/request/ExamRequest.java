package com.college.studentservices.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ExamRequest {
	@Min(value = 1, message = "Invalid College Id")
	@NotNull(message = "College Id should not be null")
	private Long collegeId;
	@NotNull(message = "Exam Center should not be null")
	private String examCenterName;
	public Long getCollegeId() {
		return collegeId;
	}
	public void setCollegeId(Long collegeId) {
		this.collegeId = collegeId;
	}
	
	public String getExamCenterName() {
		return examCenterName;
	}
	public void setExamCenterName(String examCenterName) {
		this.examCenterName = examCenterName;
	}
	
}
