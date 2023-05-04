package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User$;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.speedment.jpastreamer.application.JPAStreamer;

@Service
public class UserService {
	@Autowired
	private UserRepository userrepository;
	@Autowired
	private JPAStreamer jpaStreamer;

	public User saveUser(User user) {
		return userrepository.save(user);
	}
	public List<User> getUsersByIds(List<Long> ids) {
		return jpaStreamer.stream(User.class).filter(User$.id.in(ids)).collect(Collectors.toList());
	}
	
	public User findById(Long id) {

		return jpaStreamer.stream(User.class).filter(user -> user.getId().equals(id)).findFirst().orElseThrow();
	}
	
	public List<User> findByName(String name) {

		return jpaStreamer.stream(User.class).filter(user -> user.getName().equalsIgnoreCase(name))
				.collect(Collectors.toList());
	}
}
