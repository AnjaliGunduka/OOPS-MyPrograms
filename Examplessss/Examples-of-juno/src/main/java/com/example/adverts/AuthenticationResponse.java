package com.example.adverts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    
	private String jwt;
	

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public AuthenticationResponse() {
		super();
	}

	public AuthenticationResponse(String jwt) {
		super();
		this.jwt = jwt;
	}
	
}
