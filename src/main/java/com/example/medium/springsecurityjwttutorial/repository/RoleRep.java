package com.example.medium.springsecurityjwttutorial.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.medium.springsecurityjwttutorial.entities.Role;

public interface RoleRep extends JpaRepository<Role, Long>{

}
