package com.springboot.todo.service;

import java.util.List;

import com.springboot.todo.payload.TodoDto;

public interface TodoService {
	TodoDto createTodo(TodoDto todo);
	TodoDto updateTodo(TodoDto todo, long id);
	TodoDto getTodoById(long id);
	void deleteTodoById(long id);
	List<TodoDto> getAllTodos();
}
