package com.online.web.service;

import com.online.web.model.Authorities;

public interface AuthoritiesService {

	public void addAuthorities(Authorities authorities);
	
	Authorities findAuthoritiesByusername(String username);
}
