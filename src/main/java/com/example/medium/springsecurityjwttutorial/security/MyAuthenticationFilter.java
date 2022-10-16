package com.example.medium.springsecurityjwttutorial.security;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.medium.springsecurityjwttutorial.repository.UserRepo;



public class MyAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	private Logger logger = LoggerFactory.getLogger(MyAuthenticationFilter.class);
	
	private AuthenticationManager authenticationManager;
	
	private JWTUtil jwtUtil;
	
	private UserRepo userRepo;
	
	
	public MyAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserRepo userRepo) {
		setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login", "POST"));
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
		this.userRepo = userRepo;
	}
	
	

	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		
		
		if (!request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}
		String email = request.getParameter("email");
		email = (email != null) ? email.trim() : "";
		String password = obtainPassword(request);
		password = (password != null) ? password : "";
		UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(email,
				password);
		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);
		
		return this.authenticationManager.authenticate(authRequest);
	}



	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
	    String username = ((User) authResult.getPrincipal()).getUsername();
	  
	    Optional<com.example.medium.springsecurityjwttutorial.entities.User> myUser = this.userRepo.findByEmail(username);
	    
	    com.example.medium.springsecurityjwttutorial.entities.User user = myUser.get();
	    
	    String token = this.jwtUtil.generateToken(user, authResult);
	    
	    
	    
	    response.addHeader("Authorization", "Bearer " + token);
	    
		
	}
	

	
}
