package com.springboot.todo.config;

import java.util.Date;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

import com.springboot.todo.i18n.I18nUtil;
import com.springboot.todo.i18n.LocalHolder;

@SpringBootTest
public class MessageSourceTest {
	
	
	@Autowired
	private I18nUtil i18nUtil;
	
	@Autowired
	private LocalHolder holder;
	
	@Test
	void testMessageSource() {
		
		holder.setCurrentLocale(Locale.ENGLISH);
		String message = i18nUtil.getMessage("greeting", new String[] { new Date().toString()});
		Assertions.assertEquals("Good Morning", message);
		System.out.println(message);
	}
}
