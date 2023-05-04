package com.springs.jdbc.Controller;

public interface UserDao {
	public int registerUser(User user);

	public String loginUser(User user);
}
