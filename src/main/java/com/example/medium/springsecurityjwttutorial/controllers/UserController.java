package com.example.medium.springsecurityjwttutorial.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.medium.springsecurityjwttutorial.entities.User;
import com.example.medium.springsecurityjwttutorial.repository.UserRepo;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	
	@Autowired private UserRepo userRepo;
	
	@GetMapping("/info")
    public User getUserDetails(){
		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepo.findByEmail(email).get();
	}
}
