package com.springboot.todo.i18n;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LocaleInterceptor implements HandlerInterceptor {
	
	Logger logger = LogManager.getLogger(LocaleInterceptor.class);
	
	@Resource(name = "localHolder")
	private LocalHolder holder;
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)  throws ServletException{
		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
		logger.info("-------++++++++++++-------------- Locale value from the request: " + localeResolver.resolveLocale(request));
		if(localeResolver == null) {
			throw new IllegalStateException("No LocaleResolver found: bnot in a DispatherServlet request?");
		}
		
		if(localeResolver instanceof AcceptHeaderLocaleResolver headerLocaleResolver) {
			holder.setCurrentLocale(headerLocaleResolver.resolveLocale(request));
		} else {
			throw new IllegalStateException("Resolver should be of AccetHeaderLocaleResolver type");
		}
		
		return true;
	}
}
