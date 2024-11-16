package com.springboot.todo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.todo.payload.TodoDto;
import com.springboot.todo.service.TodoService;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
	
	private TodoService todoService;
	
	public TodoController(TodoService _service) {
		todoService = _service;
	}
	
	@PostMapping
	public ResponseEntity<TodoDto> createTodo(@RequestBody TodoDto todoDto) {
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
	public List<TodoDto> getAllTodos() {
		return todoService.getAllTodos();
	}
}
