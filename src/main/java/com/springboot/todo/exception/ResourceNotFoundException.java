package com.springboot.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{

	private String resourceName;
	private String fieldName;
	private long fieldValue;
	
	public ResourceNotFoundException(String _resourceName, String _fieldName, long _fieldValue) {
		resourceName = _resourceName;
		fieldName = _fieldName;
		fieldValue = _fieldValue;
	}

	public String getResourceName() {
		return resourceName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public long getFieldValue() {
		return fieldValue;
	}
}
