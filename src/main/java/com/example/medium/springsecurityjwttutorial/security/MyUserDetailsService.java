package com.example.medium.springsecurityjwttutorial.security;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.medium.springsecurityjwttutorial.entities.Role;
import com.example.medium.springsecurityjwttutorial.entities.User;
import com.example.medium.springsecurityjwttutorial.repository.UserRepo;

@Component
public class MyUserDetailsService implements UserDetailsService{
	
	@Autowired
	private  UserRepo userRepo;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		Optional<User> userRes = userRepo.findByEmail(email);
		
		if(userRes.isEmpty()) throw new UsernameNotFoundException("Could not find User with email = " + email);
		User user = getUser(userRes);
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		for(Role role: user.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
		}
		
		return new org.springframework.security.core.userdetails.User(email, user.getPassword(), authorities);
	}
	
	public User getUser(Optional<User> userRes) {
		User user = userRes.get();
		return user;
	}
	
	
}
