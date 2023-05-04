package com.library.repository;

import org.springframework.data.repository.CrudRepository;

import com.library.modal.User;

public interface UserRepository extends CrudRepository<User, Integer> {	
	
	public User findByUsernameAndPassword(String username, String password);
}
