package com.library.service.book.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.library.service.book.service.CustomUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private CustomUserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		/*
		 * it is resloved successfull authentication
		 * 
		 */
		final String requestTokenHeader = request.getHeader("Authorization");

		String username = null;
		String jwtToken = null;

		/*
		 * JWT Token is in the form "Bearer token". Removes Bearer word and gets only //
		 * the Token
		 */
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				System.out.println("JWT Token has expired");
			}
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}

		/*
		 * 
		 * Once we get the token validating it.
		 * 
		 */
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

			/*
			 * if token is valid configure Spring Security to manually set authentication
			 * 
			 */
			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
				/*
				 * Spring Security uses an Authentication object to represent this information
				 * and we can query this Authentication object from anywhere in our application:
				 */
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				/*
				 * SecurityContextHolder is the most fundamental object where we store details
				 * of the present security context of the application .
				 */
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		chain.doFilter(request, response);
	}

}