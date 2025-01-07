package com.springboot.todo.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.todo.payload.RegistrationRequest;
import com.springboot.todo.repository.UserRepository;
import com.springboot.todo.security.enums.Roles;
import com.springboot.todo.service.RegistrationService;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/register")
public class RegistrationController {

	@Autowired
	private RegistrationService registrationService;
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping
	public ResponseEntity<String> register(@RequestBody @Valid RegistrationRequest registrationRequest) {
		
		Set<Roles> roles = new HashSet<Roles>();
		roles.add(Roles.ROLE_USER);
		
		String registerRes = registrationService.register(registrationRequest, roles);
		
		return new ResponseEntity<String>(registerRes, HttpStatus.OK);
	}
	
	@PostConstruct
	public void init() {
		if(!registrationService.isRolePresent(Roles.ROLE_USER)) {
			registrationService.addRole(Roles.ROLE_USER);
		}
		if(!registrationService.isRolePresent(Roles.ROLE_ADMIN)) {
			registrationService.addRole(Roles.ROLE_ADMIN);
		}
		
		if(!userRepository.findByUsername("admin").isPresent()) {
			RegistrationRequest registrationRequest = new RegistrationRequest();
			registrationRequest.setUsername("admin");
			registrationRequest.setPassword("password");
			
			Set<Roles> roles = new HashSet<Roles>();
			roles.add(Roles.ROLE_ADMIN);
			
			String registerRes = registrationService.register(registrationRequest, roles);
		}
	}
}
