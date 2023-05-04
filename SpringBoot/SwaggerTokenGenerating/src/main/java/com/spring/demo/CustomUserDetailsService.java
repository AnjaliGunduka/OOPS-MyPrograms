package com.spring.demo;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;





@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * 
	 * Gets User based on Username
	 * 
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

	/**
	 * 
	 * Creates a new User
	 * 
	 * 
	 * @param createUserRequest
	 * @return
	 */
	public ApplicationUser createUser(CreateUserRequest createUserRequest) {
		return userRepository.save(new ApplicationUser(createUserRequest.getUsername(),
				passwordEncoder.encode(createUserRequest.getPassword()),
				Enum.valueOf(Role.class, createUserRequest.getRole())));
	}

}
