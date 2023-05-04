package com.library.service.book.service;

import org.springframework.stereotype.Service;

import com.library.service.book.request.CreateUserRequest;

@Service
public interface UserCommandService {

	CreateUserRequest createUser(CreateUserRequest createUserRequest);

}
