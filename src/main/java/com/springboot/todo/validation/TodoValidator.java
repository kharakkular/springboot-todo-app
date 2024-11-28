package com.springboot.todo.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.springboot.todo.payload.TodoDto;

@Component
public class TodoValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return TodoDto.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		
		TodoDto dto = (TodoDto) target;
		
		if(dto == null) {
			errors.rejectValue("TodoDto", "invalid.TodoDto", "Provided TodoDto object is null");
		}
		
		if(dto.isCompleted()) {
			errors.rejectValue("completed","invalid.completed" ,"Cannot complete todo during creation");
		}
	}
}
