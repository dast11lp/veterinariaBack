package com.example.medium.springsecurityjwttutorial.controllers;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.medium.springsecurityjwttutorial.entities.Role;
import com.example.medium.springsecurityjwttutorial.entities.User;
import com.example.medium.springsecurityjwttutorial.models.LoginCredentials;
import com.example.medium.springsecurityjwttutorial.repository.RoleRep;
import com.example.medium.springsecurityjwttutorial.repository.UserRepo;
import com.example.medium.springsecurityjwttutorial.security.JWTUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired private UserRepo userRepo;
	@Autowired private JWTUtil jwtUtil;
	@Autowired private AuthenticationManager authManager;
	@Autowired private PasswordEncoder passwordEncoder;
	@Autowired private RoleRep roleRep;
	
	@PostMapping("/register")
	public Map<String, Object> registerHandler(@RequestBody User user) throws JsonProcessingException{
		String encodedPass = this.passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPass);
		
		user = userRepo.save(user);
		
		Role role = new Role();
		role.setAuthority("ROLE_USER_" + user.getId());
		role.setidUserAuth(userRepo.findByEmail(user.getEmail()).get().getId());
		this.roleRep.save(role);
		
		String token = this.jwtUtil.generateToken(user, null);
		return Collections.singletonMap("jwt-token", token);
	}
	
	@PostMapping("/login")
	public Map<String, Object> loginHandler (@RequestBody LoginCredentials body){
		try {
			UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(body.getEmail(), body.getPassword());
			
			Optional<User> userOpt = userRepo.findByEmail(body.getEmail());
			
			User user = userOpt.get();
			
			this.authManager.authenticate(authInputToken);
			
			String token = jwtUtil.generateToken(user, null);
			
			return Collections.singletonMap("jwt-token", token);
			
		}catch (Exception authExc){
            throw new RuntimeException("Invalid Login Credentials");
        }
	}
	

}
