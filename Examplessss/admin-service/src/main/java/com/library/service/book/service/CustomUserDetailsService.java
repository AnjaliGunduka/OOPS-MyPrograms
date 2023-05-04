package com.library.service.book.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import com.library.service.book.entity.ApplicationUser;

import com.library.service.book.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	/**
	 * 
	 * 
	 * To get users from user repository based on Username
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<SimpleGrantedAuthority> roles = null;
		Optional<ApplicationUser> applicationUser = userRepository.findByUsername(username);
		if (applicationUser.isPresent()) {
			ApplicationUser user = applicationUser.get();
			roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_" + user.getRole().toString()));
			return new User(user.getUsername(), user.getPassword(), roles);
		}
		throw new UsernameNotFoundException("User not found with the Username " + username);
	}

}
