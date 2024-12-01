package com.springboot.todo.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

@Component
public class I18nUtil {

	@Autowired
	private MessageSource messageSource;
	
	@Resource(name="localHolder")
	LocalHolder holder;
	
	public String getMessage(String code, String... args) {
		return messageSource.getMessage(code, args, holder.getCurrentLocale());
	}
}
