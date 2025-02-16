package com.springboot.todo.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.spi.ErrorMessage;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.springboot.todo.entity.User;
import com.springboot.todo.exception.CustomGlobalException;
import com.springboot.todo.exception.ValidationException;
import com.springboot.todo.payload.PaginatedResponse;
import com.springboot.todo.payload.TodoDto;
import com.springboot.todo.service.TodoService;
import com.springboot.todo.validation.TodoValidator;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
	Logger logger = LogManager.getLogger(TodoController.class);
	
	private TodoService todoService;
	
	private TodoValidator validator;
	
	public TodoController(TodoService _service, TodoValidator _validator) {
		todoService = _service;
		validator = _validator;
	}
	
	@PostMapping
	public ResponseEntity<TodoDto> createTodo(@RequestBody TodoDto todoDto, BindingResult bindingResult) {
		validator.validate(todoDto, bindingResult);
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(bindingResult.hasErrors()) {
			List<String> errorMessages = bindingResult.getAllErrors().stream().map(obj -> obj.getDefaultMessage()).collect(Collectors.toList());
			
			throw new ValidationException(errorMessages);
		}
		logger.info("User value from createTodo: +++++++++++++++++++++++" + authentication.getName());
		return new ResponseEntity<TodoDto>(todoService.createTodo(todoDto, authentication.getName()), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<TodoDto> updateTodo(@RequestBody TodoDto todoDto, @PathVariable(name = "id") long id) {
		return new ResponseEntity<TodoDto>(todoService.updateTodo(todoDto, id), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteTodo(@PathVariable(name = "id") long id) {
		todoService.deleteTodoById(id);
		
		return new ResponseEntity<String>("Todo entity deleted successfully", HttpStatus.OK);
	}
	
	@GetMapping
	public PaginatedResponse<TodoDto> getAllTodos(@AuthenticationPrincipal String user, @RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
		String userName = getAuthentication().getName();
		logger.info("////////////////////////////User information from TodoController: " + user + ", Username: " + userName);
		return todoService.getAllTodos(userName, pageNo, pageSize);
	}
	
	@GetMapping(params = { "creationDate"})
	public DeferredResult<PaginatedResponse<TodoDto>> getAllTodosByCreationDate(Principal principal, @RequestParam(name = "creationDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date creationDate, 
									@RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
		DeferredResult<PaginatedResponse<TodoDto>> dfr = new DeferredResult<PaginatedResponse<TodoDto>>();
		logger.info("12 12 12 12 12 Princial value from getAllTodosByCreationDate: " + principal.getName());
		try {
			todoService.getTodosByCreationDateAsync(principal.getName(), creationDate, pageNo, pageSize)
						.thenAccept(res -> dfr.setResult(res))
						.exceptionally(ex -> { 
							dfr.setErrorResult(new CustomGlobalException(ex.getMessage()));
							return null;
						});
		} catch (InterruptedException e) {
			throw new CustomGlobalException(e.getMessage());
		}
		
		return dfr;
	}
	
	@GetMapping(params = { "completion" })
	public Callable<PaginatedResponse<TodoDto>> getAllTodosByCompletion(@AuthenticationPrincipal String user, @RequestParam(name = "completion") boolean completion, 
			@RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "0") int pageSize){
		
		Callable<PaginatedResponse<TodoDto>> callable = todoService.getAllTodosbyCompletion(user, completion, pageNo, pageSize);
		
		try {
			callable.call();
			
		} catch (Exception e) {
			throw new CustomGlobalException(e.getMessage());
		}
		
		return callable;
	}
	
	private Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
}

