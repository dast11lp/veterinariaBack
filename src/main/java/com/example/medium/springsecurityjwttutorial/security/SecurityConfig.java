package com.example.medium.springsecurityjwttutorial.security;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.medium.springsecurityjwttutorial.repository.UserRepo;

@Configuration
public class SecurityConfig {
	
	@Autowired private UserRepo userRepo;
	@Autowired private JWTFilter filter;
	@Autowired private MyUserDetailsService userDetailsService;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		
		http.csrf().disable()
			.httpBasic().disable()
			.cors()
			.and()
			.authorizeHttpRequests()
			.antMatchers("/api/auth/**").permitAll()
			.antMatchers("/api/user/**").hasRole("USER_" + this.roleId())
			.and()
			.userDetailsService(this.userDetailsService)
			.exceptionHandling()
				.authenticationEntryPoint(null)
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
			
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}
	
	public Long roleId () {
		return (long) 1;
	}

}
