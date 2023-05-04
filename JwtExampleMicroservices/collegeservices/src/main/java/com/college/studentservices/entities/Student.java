package com.college.studentservices.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "STUDENT")
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	@Column(name = "STUDENT_NUMBER")
	private String studentNumber;
	@Column(name = "TYPE")
	private String type;
	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	private Status status;
	@Column(name = "STUDENT_NAME")
	private String studentName;
	@Column(name = "EXAM_CENTER_ID")
	private String examcenterId;
	@Column(name = "COLLEGE_CODE")
	private String collegeCode;
	@Column(name = "EXAM_CENTER")
	private String examCenter;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COLLEGE_ID", nullable=false)
	private College college;
	public Student() {
		super();
	}
	public Student(String studentNumber, String type, Status status, String studentName, String examcenterId,
			String collegeCode, String examCenter, College college) {
		super();
		this.studentNumber = studentNumber;
		this.type = type;
		this.status = status;
		this.studentName = studentName;
		this.examcenterId = examcenterId;
		this.collegeCode = collegeCode;
		this.examCenter = examCenter;
		this.college = college;
	}
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
	public College getCollege() {
		return college;
	}
	public void setCollege(College college) {
		this.college = college;
	}
	public void setExamCenter(boolean equals) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
}
