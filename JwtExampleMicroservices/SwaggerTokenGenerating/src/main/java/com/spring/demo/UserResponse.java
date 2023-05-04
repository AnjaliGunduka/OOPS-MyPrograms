package com.spring.demo;

/**
 * 
 * 
 * 
 * @author Anjali
 *
 */
public class UserResponse {
	private String username;
	private String role;

	public UserResponse(String username, String role) {
		super();
		this.username = username;
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
