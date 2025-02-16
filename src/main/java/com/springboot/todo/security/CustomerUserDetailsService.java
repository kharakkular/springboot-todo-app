package com.springboot.todo.security;

import static org.mockito.Mockito.CALLS_REAL_METHODS;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.springboot.todo.entity.User;
import com.springboot.todo.repository.UserRepository;

import jakarta.transaction.Transactional;

@Component
public class CustomerUserDetailsService implements UserDetailsService{
	Logger logger = LogManager.getLogger(CustomerUserDetailsService.class);
	
	@Autowired
	public UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user not found"));
		logger.info("+++++-----***** User details from CustomerUserDetailsService: " + user.getRoles().size());
		return new org.springframework.security.core.userdetails.User(
				user.getUsername(), 
				user.getPassword(), 
				user.isEnabled(), 
				true, true, true, 
				user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList()));
	}
}
