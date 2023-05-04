package com.library.service.book.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.library.service.book.entity.ApplicationUser;
import com.library.service.book.enums.Role;
import com.library.service.book.repository.UserRepository;
import com.library.service.book.request.CreateUserRequest;
@Service
public class UserServiceImpl implements UserCommandService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@Override
	public CreateUserRequest createUser(CreateUserRequest userCreateDto) {
		ApplicationUser user = new ApplicationUser();
		user.setUsername(userCreateDto.getUsername());
		user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
		user.setRole(Role.ADMIN);
		user = userRepository.save(user);
		return new CreateUserRequest(user.getUsername(), user.getPassword(),Role.ADMIN);
	}

	

	
}
