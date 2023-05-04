package com.student.library.services.service;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.student.library.services.client.BookServiceClient;
import com.student.library.services.request.JwtRequest;
import com.student.library.services.response.JWTTokenResponse;

@Service
public class AuthService {

	@Autowired
	BookServiceClient bookServiceClient;

	@Value("${bookService.auth.username}")
	private String username;

	@Value("${bookService.auth.password}")
	private String password;

	/**
	 * 
	 * Gets Auth Token to call Book Service
	 * 
	 * @return
	 */
	public String getAuthToken() {
		JWTTokenResponse tokenResponse = bookServiceClient
				.getAuthToken(new JwtRequest(username, decodePassword(password)));
		return "Bearer " + tokenResponse.getAccessToken();
	}

	/**
	 * 
	 * Decodes the Base64 Encoded Password
	 * 
	 * @param encodedPassword
	 * @return
	 */
	private String decodePassword(String encodedPassword) {
		Base64.Decoder decoder = Base64.getMimeDecoder();
		String to = new String(decoder.decode(encodedPassword));
		return to;
	}

}
