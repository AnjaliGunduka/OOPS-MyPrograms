package com.student.library.services.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class StudentRequest {
	@NotBlank(message = "Branch Name should not be null or Empty")
	private String branch;
	@NotBlank(message = "User Name should not be null or Empty")
	private String userName;
	@NotBlank(message = "Password should not be null or Empty")
	private String password;
	@NotBlank(message = "Semester should not be null or Empty")
	@Size(min = 1, max = 1, message = "Semester Length must be 1 characters")
	private int semester;
	private String email;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
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
	public int getSemester() {
		return semester;
	}
	public void setSemester(int semester) {
		this.semester = semester;
	}
	
	
	
}
