package com.springboot.todo.exception;

public class CustomGlobalException extends RuntimeException{

	private String error;
	
	public CustomGlobalException(String errorMessage) {
		super(errorMessage);
		this.error = errorMessage;
	}
	
	public String getErrorMessage() {
		return this.error;
	}
}
