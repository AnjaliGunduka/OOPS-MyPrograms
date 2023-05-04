package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.demo.entity.User;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	 @Autowired
	 UserService userService;
	 
	 
	 
	 
	 @PostMapping
	    public User saveUser(@RequestBody User user){
	        return userService.saveUser(user);
	    }
	 
	 @PostMapping("/ids")
	    public  List<User> getUsersByIds(@RequestBody List<Long> ids){
	        return userService.getUsersByIds(ids);
	    }
	 
}
