package com.online.web.service;

import com.online.web.model.Users;

public interface UsersService {

	public void addUsers(Users users);
	
	Users findUserByusername(String username);
}
