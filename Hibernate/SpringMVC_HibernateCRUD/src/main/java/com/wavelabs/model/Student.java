package com.wavelabs.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Student")
public class Student {

	@Id
	@Column(name = "studId")
	String studId;

	@Column(name = "studName")
	String studName;

	@Column(name = "marks")
	String marks;

	public Student() {
		super();
	}

	public Student(String studid, String studName, String marks) {
		super();
		this.studId = studid;
		this.studName = studName;
		this.marks = marks;
	}

	public String getStudId() {
		return studId;
	}

	public void setStudId(String studid) {
		this.studId = studid;
	}

	public String getStudName() {
		return studName;
	}

	public void setStudName(String studName) {
		this.studName = studName;
	}

	public String getMarks() {
		return marks;
	}

	public void setMarks(String marks) {
		this.marks = marks;
	}

}
