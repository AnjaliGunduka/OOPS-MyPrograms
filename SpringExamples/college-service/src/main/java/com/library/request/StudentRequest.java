package com.library.request;

import javax.validation.constraints.NotBlank;



public class StudentRequest {
	@NotBlank(message = "Student first Name should not be null or Empty")
	private String firstName;
	@NotBlank(message = "Student Last Name should not be null or Empty")
	private String lastName;
	@NotBlank(message = "Branch  should not be null or Empty")
	private String branch;
	@NotBlank(message = "Semster  should not be null or Empty")
	private String semester;
	@NotBlank(message = "Password  should not be null or Empty")
	private String password;
	@NotBlank(message = "Username should not be null or Empty")
	private String userName;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	
	public String getSemester() {
		return semester;
	}
	public void setSemester(String semester) {
		this.semester = semester;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
