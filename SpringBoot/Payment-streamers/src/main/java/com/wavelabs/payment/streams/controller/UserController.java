package com.wavelabs.payment.streams.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wavelabs.payment.streams.model.User;
import com.wavelabs.payment.streams.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping
	public User saveUser(@RequestBody User user) {
		return userService.saveUser(user);
	}

	@GetMapping
	public ResponseEntity<List<User>> findAllUsers(@RequestParam(value = "name") String name) {

		if (null != name) {

			return ResponseEntity.ok(userService.findByName(name));

		}
		return ResponseEntity.ok(userService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {

		return ResponseEntity.ok(userService.findById(id));

	}

}
