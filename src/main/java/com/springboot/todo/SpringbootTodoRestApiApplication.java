package com.springboot.todo;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringbootTodoRestApiApplication extends SpringBootServletInitializer{

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SpringbootTodoRestApiApplication.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SpringbootTodoRestApiApplication.class, args);
	}
}
