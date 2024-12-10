package com.springboot.todo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private CustomAuthenticationEntryPoint authenticationEntryPoint;
	
	public SecurityConfig(CustomAuthenticationEntryPoint _CustomAuthenticationEntryPoint) {
		this.authenticationEntryPoint = _CustomAuthenticationEntryPoint;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.
			csrf().disable()
			.authorizeRequests()
			.anyRequest().authenticated()
			.and()
			.httpBasic()
			.authenticationEntryPoint(authenticationEntryPoint);
		return http.build();
	}
	
	@SuppressWarnings("deprecation")
	@Bean
	public UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager detailsManager = new InMemoryUserDetailsManager();
		detailsManager.createUser(User.builder().username("user").password(passwordEncoder().encode("password")).roles("ADMIN").build());
		return detailsManager;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
