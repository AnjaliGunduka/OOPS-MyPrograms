package com.jwt.token.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jwt.token.exception.CustomException;

// We should use OncePerRequestFilter since we are doing a database call, there is no point in doing this more than once
public class JwtTokenFilter extends OncePerRequestFilter {

  private JwtTokenProvider jwtTokenProvider;

  public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
    //it is resloved successfull authentication
	  String token = jwtTokenProvider.resolveToken(httpServletRequest);
    try {
      if (token != null && jwtTokenProvider.validateToken(token)) {
    	  /*    Spring Security uses an Authentication object to 
    	   * represent this information and we can 
    	   * query this Authentication object from anywhere
    	   *  in our application:*/
        Authentication auth = jwtTokenProvider.getAuthentication(token);
        /*SecurityContextHolder is the most fundamental object where we store 
        details of the present security context of the application 
        (includes details of the principal).*/
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    } catch (CustomException ex) {
      //this is very important, since it guarantees the user is not authenticated at all
      SecurityContextHolder.clearContext();
      httpServletResponse.sendError(ex.getHttpStatus().value(), ex.getMessage());
      return;
    }

    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }

}
