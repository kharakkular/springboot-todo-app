package com.springboot.todo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
	
	private UserDetailsService userDetailsService;
	
	public SecurityConfig(CustomAuthenticationEntryPoint _CustomAuthenticationEntryPoint, UserDetailsService detailsService) {
		this.authenticationEntryPoint = _CustomAuthenticationEntryPoint;
		this.userDetailsService = detailsService;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.
			csrf().disable()
			.authorizeHttpRequests(authorize -> authorize
					.requestMatchers("/api/register").permitAll()
					.anyRequest().authenticated()
					)
			.httpBasic();
		
		return http.build();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(userDetailsService);
		return provider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
	
//	@SuppressWarnings("deprecation")
//	@Bean
//	public UserDetailsService userDetailsService() {
//		InMemoryUserDetailsManager detailsManager = new InMemoryUserDetailsManager();
//		detailsManager.createUser(User.builder().username("user").password(passwordEncoder().encode("password")).roles("ADMIN").build());
//		return detailsManager;
//	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
