package com.springboot.todo.exception;

import java.util.Date;

public class FutureCreationDateException extends RuntimeException{

	private String resourceName;
	private String fieldName;
	private Date field;
	
	public FutureCreationDateException(String _resourceName, String _fieldName, Date _field) {
		resourceName = _resourceName;
		fieldName = _fieldName;
		field = _field;
	}
	
	public String getResourceName() {
		return resourceName;
	}
	
	public String getFeildName() {
		return fieldName;
	}
	
	public Date getField() {
		return field;
	}
}
