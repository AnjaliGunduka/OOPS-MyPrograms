package com.library.service.book.test.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import com.library.service.book.config.JwtAuthenticationEntryPoint;
import com.library.service.book.config.JwtTokenUtil;
import com.library.service.book.entity.ApplicationUser;
import com.library.service.book.enums.Role;
import com.library.service.book.repository.UserRepository;
import com.library.service.book.request.CreateUserRequest;
import com.library.service.book.service.CustomUserDetailsService;
import com.library.service.book.service.UserCommandService;

@WebMvcTest(UserCommandService.class)
public class AuthenticationServiceTest {
	@MockBean
    UserRepository userRepository;

    @Autowired
    private UserCommandService userCommandService;

    @MockBean
    private CustomUserDetailsService userDetailsServiceImpl;

    @MockBean
    private JwtTokenUtil jwtUtil;
    @MockBean
    JwtAuthenticationEntryPoint  jwtAuthenticationEntryPoint;
    @Test
    void testCreateUser() { 
    	ApplicationUser userMocked = new ApplicationUser(1L, "user", "test",Role.ADMIN);
    	CreateUserRequest userCreateDto = new CreateUserRequest("user", "test", "ADMIN");
        when(userRepository.save(any(ApplicationUser.class))).thenReturn(userMocked);
        userCreateDto = userCommandService.createUser(userCreateDto);
        assertNotNull(userCreateDto);
        assertEquals(userMocked.getUsername(), userCreateDto.getUsername());
        assertEquals(userMocked.getPassword(), userCreateDto.getPassword());
      
    }
    
    
    

}
