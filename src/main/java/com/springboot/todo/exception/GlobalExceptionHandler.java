package com.springboot.todo.exception;

import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.springboot.todo.i18n.I18nUtil;
import com.springboot.todo.payload.ErrorDetails;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
	
	@Autowired
	private I18nUtil i18nUtil;
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest request) {
		
		ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
		
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler({ CustomGlobalException.class, Exception.class })
	public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception, WebRequest request) {
		
		ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
		
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@Override
	public ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request){
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
		
		return new ResponseEntity<>(errorDetails, HttpStatusCode.valueOf(status.value()));
	}
	
	@ExceptionHandler(FutureCreationDateException.class)
	public ResponseEntity<ErrorDetails> handleFutureCreationDateException(FutureCreationDateException ex, WebRequest request){
		
		Locale locale = Locale.getDefault();
		String[] params = { ex.getField().toString() };
		String localizedMessage =  i18nUtil.getMessage("future.exception", params);
		ErrorDetails errorDetails = new ErrorDetails(new Date(), localizedMessage, request.getDescription(false));
		
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ErrorDetails> handleValidationException(ValidationException ex, WebRequest request) {
		
		ErrorDetails errorDetails = new ErrorDetails(new Date(), "Validation Error for TodoDto **completion**, {" + ex.getErrors().get(0)+ "}", request.getDescription(false));
		
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST);
	}
}
