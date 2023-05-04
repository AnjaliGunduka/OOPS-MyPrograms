package com.student.library.services.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "STUDENT")
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	@Column(name = "USERNAME")
	private String userName;
	@Column(name = "PASSWORD")
	private String password;
	@Column(name = "BRANCH")
	private String branch;
	@Column(name = "SEMESTER")
	private int semester;
	@Column(name = "CARDNO")
	private String cardNo;
	@Column(name = "EMAIL")
	private String email;

	public Student() {
		super();
	}

	public Student(Long id, String userName, String password, String branch, int semester, String cardNo,
			String email) {
		super();
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.branch = branch;
		this.semester = semester;
		this.cardNo = cardNo;
		this.email = email;
	}

	public Student(String cardNo, String userName, String password, String branch, int semester, String email) {
		super();
		this.cardNo = cardNo;
		this.userName = userName;
		this.password = password;
		this.branch = branch;
		this.semester = semester;
		this.email = email;
	}
	

	public Student(String userName, String password, String branch, int semester, String cardNo, String email) {
		super();
		this.userName = userName;
		this.password = password;
		this.branch = branch;
		this.semester = semester;
		this.cardNo = cardNo;
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public int getSemester() {
		return semester;
	}

	public void setSemester(int semester) {
		this.semester = semester;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
