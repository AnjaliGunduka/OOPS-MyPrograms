package com.studentcollege.response;

import java.time.Instant;

/**
 * 
 * 
 * @author muralikrishnak
 *
 */
public class CenterResponse {
	private Long id;
	private String studentNumber;
	private String studentName;
	private String CollegeName;
	private String examCenter;
	private String examCenterName;
	private Instant date;
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
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
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
	public String getExamCenterName() {
		return examCenterName;
	}
	public void setExamCenterName(String examCenterName) {
		this.examCenterName = examCenterName;
	}
	public Instant getDate() {
		return date;
	}
	public void setDate(Instant date) {
		this.date = date;
	}

	
}
