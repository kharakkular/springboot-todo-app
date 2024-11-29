package com.springboot.todo.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.spi.ErrorMessage;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.springboot.todo.exception.ValidationException;
import com.springboot.todo.payload.PaginatedResponse;
import com.springboot.todo.payload.TodoDto;
import com.springboot.todo.service.TodoService;
import com.springboot.todo.validation.TodoValidator;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
	
	private TodoService todoService;
	
	private TodoValidator validator;
	
	public TodoController(TodoService _service, TodoValidator _validator) {
		todoService = _service;
		validator = _validator;
	}
	
	@PostMapping
	public ResponseEntity<TodoDto> createTodo(@RequestBody TodoDto todoDto, BindingResult bindingResult) {
		validator.validate(todoDto, bindingResult);
		
		if(bindingResult.hasErrors()) {
			List<String> errorMessages = bindingResult.getAllErrors().stream().map(obj -> obj.getDefaultMessage()).collect(Collectors.toList());
			
			throw new ValidationException(errorMessages);
		}
		return new ResponseEntity<TodoDto>(todoService.createTodo(todoDto), HttpStatus.CREATED);
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
	public PaginatedResponse<TodoDto> getAllTodos(@RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
		return todoService.getAllTodos(pageNo, pageSize);
	}
}
