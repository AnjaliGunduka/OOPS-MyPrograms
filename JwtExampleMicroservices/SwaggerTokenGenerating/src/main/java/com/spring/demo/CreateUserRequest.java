package com.spring.demo;

import javax.validation.constraints.NotBlank;



public class CreateUserRequest {
	@NotBlank(message = "Username should not be null or Empty")
	private String username;
	@NotBlank(message = "Password should not be null or Empty")
	private String password;
	@NotBlank(message = "Role should not be null or Empty")
	@EnumValidator(message = "Invalid Role", enumType = Role.class)
	private String role;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
