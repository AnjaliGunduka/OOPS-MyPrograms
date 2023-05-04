package com.studentcollege.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CENTER")
public class Center {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	@Column(name = "STUDENT_NUMBER")
	private String studentNumber;
	@Column(name = "STUDENT_NAME")
	private String studentName;
	@Column(name = "EXAM_CENTER_NAME")
	private String examCenterName;
	@Column(name = "COLLEGE_NAME")
	private String collegeName;
	@Column(name = "DATE_TIME")
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
	public String getExamCenterName() {
		return examCenterName;
	}
	public void setExamCenterName(String examCenterName) {
		this.examCenterName = examCenterName;
	}
	public String getCollegeName() {
		return collegeName;
	}
	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}
	public Instant getDate() {
		return date;
	}
	public void setDate(Instant date) {
		this.date = date;
	}
	public Center(String studentNumber, String studentName, String examCenterName, String collegeName, String string, Instant date) {
		super();
		this.studentNumber = studentNumber;
		this.studentName = studentName;
		this.examCenterName = examCenterName;
		this.collegeName = collegeName;
		this.date = date;
	}
	public Center() {
		super();
	}
	
	
}
