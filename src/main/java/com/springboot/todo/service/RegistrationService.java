package com.springboot.todo.service;

import java.util.Set;

import com.springboot.todo.payload.RegistrationRequest;
import com.springboot.todo.security.enums.Roles;

public interface RegistrationService {
	String register(RegistrationRequest registrationRequest, Set<String> roles);
	boolean isRolePresent(Roles role);
	void addRole(Roles role);
}
