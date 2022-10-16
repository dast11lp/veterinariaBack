package com.example.medium.springsecurityjwttutorial.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.medium.springsecurityjwttutorial.entities.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;



@Component
public class JWTUtil {
	
	@Value("${jwt_secret}")
	private String secret;

	
	@Transactional(readOnly = true)
	public String generateToken(User user, Authentication auth) throws JsonProcessingException {
	  
	      
	  Map<String, Object> claims = new HashMap<>();
	  
	  
	  if (auth!=null) 
	  claims.put("authorities", new ObjectMapper().writeValueAsString(auth.getAuthorities()));
	  claims.put("email", user.getEmail());

	  
		return JWT.create()
				.withSubject("User Details")
				.withClaim("claims", claims)
				.withIssuedAt(new Date())
				.withIssuer("YOUR APPLICATION/PROJECT/COMPANY NAME")
				.sign(Algorithm.HMAC256(secret));
	}
	
	public String validateTokenAndRetrieveSubject (String token) {
		
		JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
							   .withSubject("User Details")
							   .withIssuer("YOUR APPLICATION/PROJECT/COMPANY NAME")
							   .build();
		DecodedJWT jwt = verifier.verify(token);
		
		return jwt.getClaim("email").asString();
	
	}
}
