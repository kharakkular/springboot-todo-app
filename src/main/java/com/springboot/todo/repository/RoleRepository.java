package com.springboot.todo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.todo.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
	Optional<Role> findByRole(String role);
}
