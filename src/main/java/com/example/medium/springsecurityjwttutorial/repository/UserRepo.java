package com.example.medium.springsecurityjwttutorial.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.medium.springsecurityjwttutorial.entities.User;

public interface UserRepo extends JpaRepository<User, Long>{
	
	public Optional<User> findByEmail(String email);
	
}
