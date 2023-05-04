package com.library.service.book.request;

import javax.validation.constraints.NotBlank;

import com.library.service.book.enums.Role;

public class CreateUserRequest {
	@NotBlank(message = "Username should not be null or Empty")
	private String username;
	@NotBlank(message = "Password should not be null or Empty")
	private String password;
	@NotBlank(message = "Role should not be null or Empty")
	private String role;

	public CreateUserRequest(@NotBlank(message = "Username should not be null or Empty") String username,
			@NotBlank(message = "Password should not be null or Empty") String password,
			@NotBlank(message = "Role should not be null or Empty") Role role) {
		super();
		this.username = username;
		this.password = password;
		this.role = getRole();
	}

	

	public CreateUserRequest(@NotBlank(message = "Username should not be null or Empty") String username,
			@NotBlank(message = "Password should not be null or Empty") String password,
			@NotBlank(message = "Role should not be null or Empty") String role) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
	}



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
