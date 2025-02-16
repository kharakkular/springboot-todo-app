package com.springboot.todo.interceptor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoggingInterceptor implements HandlerInterceptor {
	private static final Logger logger = LogManager.getLogger(LoggingInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//		logger.info("********** User details from LoggingInterceptor: " + userDetails.getAuthorities().size());
		logger.info("================== Handler value: " + handler);
		return true;
	}
}
