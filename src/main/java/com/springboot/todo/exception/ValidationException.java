package com.springboot.todo.exception;

import java.util.List;

public class ValidationException extends RuntimeException{
	
	private List<String> errors;
	
	public ValidationException(List<String> _errors) {
		super("Validation failed");
		errors = _errors;
	}
	
	public List<String> getErrors() {
		return errors;
	}
}
