package com.springboot.todo.service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

import com.springboot.todo.payload.PaginatedResponse;
import com.springboot.todo.payload.TodoDto;

public interface TodoService {
	TodoDto createTodo(TodoDto todo);
	TodoDto updateTodo(TodoDto todo, long id);
	TodoDto getTodoById(long id);
	void deleteTodoById(long id);
	PaginatedResponse<TodoDto> getAllTodos(int pageNo, int pageSize);
	CompletableFuture<PaginatedResponse<TodoDto>> getTodosByCreationDateAsync(Date creationDate, int pageNo, int pageSize) throws InterruptedException;
	Callable<PaginatedResponse<TodoDto>> getAllTodosbyCompletion(boolean completion, int pageNo, int pageSize);
}
