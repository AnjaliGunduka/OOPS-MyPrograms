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
	@Column(name = "TYPE")
	private String type;
	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	private Status status;
	@Column(name = "STUDENT_NAME")
	private String studentName;
	@Column(name = "COLLEGE_CODE")
	private String collegeCode;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COLLEGE_ID", nullable=false)
	private College college;
	
	public Student() {
		super();
	}
	public Student( String type, String string, Status status, String studentName, 
			 College college) {
		super();
		
		this.type = type;
		this.status = status;
		this.studentName = studentName;
	
		this.collegeCode = collegeCode;
		
		this.college = college;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	
	public String getCollegeCode() {
		return collegeCode;
	}
	public void setCollegeCode(String collegeCode) {
		this.collegeCode = collegeCode;
	}
	
	public College getCollege() {
		return college;
	}
	public void setCollege(College college) {
		this.college = college;
	}
	
	
	
	
	
	
	
}
