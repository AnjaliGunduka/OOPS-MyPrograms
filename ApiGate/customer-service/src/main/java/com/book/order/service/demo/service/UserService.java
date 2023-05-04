package com.book.order.service.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.book.order.service.demo.model.User;
import com.book.order.service.demo.repository.UserRepository;


@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

 

  public String signin(String username, String password) {
	return username;
    
  }

  public User signup(User user) {
	return user;
    
  }

 

}
