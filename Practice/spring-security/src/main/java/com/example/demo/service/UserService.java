package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.common.AesEncryption;
import com.example.demo.common.Constants;
import com.example.demo.entity.AuthenticationDetails;
import com.example.demo.entity.LoginCredentials;
import com.example.demo.entity.UserAddress;
import com.example.demo.entity.Users;
import com.example.demo.enums.Status;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.RecordNotfoundException;
import com.example.demo.mapper.LoginMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.AuthenticationDetailsRepository;
import com.example.demo.repository.LoginCredentialsRepository;
import com.example.demo.repository.UserAddressRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.CreateUserRequest;
import com.example.demo.request.LoginRequest;
import com.example.demo.response.LoginResponse;
import com.example.demo.utils.TokenPayLoadDetails;

@Service
public class UserService {

	private static Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserAddressRepository userAddressRepository;

	@Autowired
	AesEncryption aesEncryption;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	LoginCredentialsRepository loginCredentialsRepository;

	@Autowired
	JwtAuthenticationService jwtAuthenticationService;

	@Autowired
	AuthenticationDetailsRepository authenticationDetailsRepository;

	public LoginResponse signUpUser(CreateUserRequest userRequest) {

		Optional<Users> userOptional = userRepository.findByEmail(userRequest.getEmailId());
		if (userOptional.isPresent()) {
			throw new BadRequestException(Constants.USER_ALREADY_PRESENT);
		}

		Users user = new Users();
		user = UserMapper.getUser(userRequest, user);
		logger.info("Saving User basic deatils");
		userRepository.save(user);

		UserAddress userAddress = UserMapper.getUserAddress(userRequest, new UserAddress(), user);
		logger.info("Saving User Address deatils to db with id :: {}", user.getId());
		userAddressRepository.save(userAddress);

		LoginCredentials userCredentials = UserMapper.getUserCredentials(userRequest, new LoginCredentials(),
				passwordEncoder.encode(userRequest.getPassword()), user);

		loginCredentialsRepository.save(userCredentials);

		logger.info("Saving user credentials to db with id:: {}", userCredentials.getId());

		String token = jwtAuthenticationService.generateToken(user.getId(), userRequest.getRole().toString(), user.getFullName(),
				user.getEmail());
		AuthenticationDetails authenticationDetails = LoginMapper.getAuthenticationDetails(user, token);

		authenticationDetailsRepository.save(authenticationDetails);
		return LoginMapper.getLoginResponse(user.getId(), Constants.SUCCESS, Constants.USER_CREATED, token);
	}

	public LoginResponse login(LoginRequest request) {
		logger.info("login method started");
		Optional<Users> userOptional = userRepository.findByEmail(request.getEmailId());
		if (!userOptional.isPresent()) {

			throw new RecordNotfoundException(Constants.USER_NOT_FOUND_WITH_THIS_EMAIL);
		}

		Optional<LoginCredentials> loginOptional = loginCredentialsRepository.findByUserId(userOptional.get());
		if (!loginOptional.isPresent()) {
			throw new RecordNotfoundException(Constants.USER_CREDENTIALS_NOT_FOUND);
		}
		if (!request.getEmailId().equalsIgnoreCase(loginOptional.get().getUsername())) {
			throw new RecordNotfoundException(Constants.INVALID_EMAIL);
		}

		boolean password = passwordEncoder.matches(request.getPassword(), loginOptional.get().getPassword());
		if (!password) {
			throw new RecordNotfoundException(Constants.INVALID_PASSWORD);
		}

		try {
			Optional<AuthenticationDetails> authOptional = authenticationDetailsRepository
					.findByUserIdAndStatus(userOptional.get(), Status.ACTIVE);
			if (authOptional.isPresent()) {
				authOptional.get().setStatus(Status.INACTIVE);
				authenticationDetailsRepository.save(authOptional.get());
			}
		} catch (Exception exception) {
			List<AuthenticationDetails> authUsers = authenticationDetailsRepository.findByUserId(userOptional.get());
			authUsers.forEach(auth -> auth.setStatus(Status.INACTIVE));
			authenticationDetailsRepository.saveAll(authUsers);
		}

		logger.info("login method ended");
		return getUserLoginResponse(userOptional.get(), Constants.USER_LOGIN_SUCCESSFULLY);

	}

	private LoginResponse getUserLoginResponse(Users users, String message) {
		String token = jwtAuthenticationService.generateToken(users.getId(), Constants.USER, users.getFullName(),
				users.getEmail());
		logger.info("Jwt token generated successfully");
		AuthenticationDetails authenticationDetails = LoginMapper.getAuthenticationDetails(users, token);

		authenticationDetailsRepository.save(authenticationDetails);
		logger.info("Authentication details are saved.");

		return LoginMapper.getUserLoginResponse(users.getId(), Constants.SUCCESS, message, token);

	}

	public Users getUser(long id, TokenPayLoadDetails details) {
		if("USER".equals(details.getTypeOfUser())) {
			Users user = userRepository.findById(id).orElse(null);
			if (user == null) {
				throw new BadRequestException(Constants.NO_VALUE_PRESENT);
			}
			return user;
		}
		else {
			throw new BadRequestException(Constants.NO_ACCESS);
		}
		
	}
}
