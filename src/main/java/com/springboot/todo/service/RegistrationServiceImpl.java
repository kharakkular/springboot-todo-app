package com.springboot.todo.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.todo.entity.Role;
import com.springboot.todo.entity.User;
import com.springboot.todo.payload.RegistrationRequest;
import com.springboot.todo.repository.RoleRepository;
import com.springboot.todo.repository.UserRepository;
import com.springboot.todo.security.enums.Roles;

@Service
public class RegistrationServiceImpl implements RegistrationService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public String register(RegistrationRequest registrationRequest, Set<Roles> roles) {
		
		if(userRepository.findByUsername(registrationRequest.getUsername()).isPresent()) {
			throw new RuntimeException("Username already exists");
		}
		
		User newUser = new User();
		newUser.setUsername(registrationRequest.getUsername());
		newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
		
		Set<Role> usersRoles = new HashSet<Role>();
		
		for(Roles role: roles) {
			Optional<Role> retrievedRole = roleRepository.findByRole(role.toString());
			if(retrievedRole.isPresent()) {
				usersRoles.add(retrievedRole.get());
			}
		}
		
		newUser.setRoles(usersRoles);
		
		User savedUser = userRepository.save(newUser);
		
		return "User: " + savedUser.getUsername() + ", successfully registered";
	}

	@Override
	public boolean isRolePresent(Roles role) {
		Optional<Role> retrievedRole = roleRepository.findByRole(role.toString());
		return retrievedRole.isPresent();
	}

	@Override
	public void addRole(Roles role) {
		Role newRole = new Role();
		newRole.setRole(role.toString());
		
		roleRepository.save(newRole);
	}
	
}
