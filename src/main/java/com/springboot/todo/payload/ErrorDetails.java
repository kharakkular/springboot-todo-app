package com.springboot.todo.payload;

import java.util.Date;

public class ErrorDetails {
	private Date timeStamp;
	private String message;
	private String details;
	
	public ErrorDetails(Date _timeStamp, String _message, String _details) {
		timeStamp = _timeStamp;
		message = _message;
		details = _details;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return details;
	}
}
