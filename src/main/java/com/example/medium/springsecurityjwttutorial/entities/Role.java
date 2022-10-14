package com.example.medium.springsecurityjwttutorial.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "authorities")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_auth")
	private Long id;

	@Column(name = "id_user_auth")
	private Long idUserAuth;

	private String authority;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getidUserAuth() {
		return idUserAuth;
	}

	public void setidUserAuth(Long userId) {
		this.idUserAuth = userId;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

}
