package com.springboot.todo.service;

//import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.springboot.todo.entity.Todo;
import com.springboot.todo.exception.ResourceNotFoundException;
import com.springboot.todo.payload.PaginatedResponse;
import com.springboot.todo.payload.TodoDto;
import com.springboot.todo.repository.TodoRepository;

@Service
public class TodoServiceImpl implements TodoService{
	
	private TodoRepository todoRepository;
	
	private ModelMapper mapper;
	
	public TodoServiceImpl(TodoRepository _todoRepository, ModelMapper _mapper) {
		todoRepository = _todoRepository;
		mapper = _mapper;
	}

	@Override
	public TodoDto createTodo(TodoDto todoDto) {
		
		Todo todo = mapToEntity(todoDto);
		Todo postTodo = todoRepository.save(todo);
		
		TodoDto postResponse = mapToDto(postTodo);
		return postResponse;
	}

	@Override
	public TodoDto updateTodo(TodoDto todoDto, long id) {
		
		Todo fetchedTodo = todoRepository.findById(id).
				orElseThrow(() -> new ResourceNotFoundException("Todo", "id", todoDto.getId()));
		
		fetchedTodo.setCompleted(todoDto.isCompleted());
		fetchedTodo.setTask(todoDto.getTask());
		
		Todo postTodo = todoRepository.save(fetchedTodo);
		
		return mapToDto(postTodo);
	}

	@Override
	public TodoDto getTodoById(long id) {
		
		Todo fetchedTodo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo", "id", id));
		
		return mapToDto(fetchedTodo);
	}

	@Override
	public void deleteTodoById(long id) {
		
		Todo fetchedTodo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
		
		todoRepository.delete(fetchedTodo);
	}

	@Override
	public PaginatedResponse<TodoDto> getAllTodos(int pageNo, int pageSize) {
		
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		
		Page<Todo> pagedTodos = todoRepository.findAll(pageable);
		
		List<Todo> listOfTodos = pagedTodos.getContent();
		
		List<TodoDto> content = listOfTodos.stream().map(todo -> mapToDto(todo)).toList();
		
		PaginatedResponse<TodoDto> paginatedResponse = new PaginatedResponse();
		paginatedResponse.setContent(content);
		paginatedResponse.setPageNo(pagedTodos.getNumber());
		paginatedResponse.setPageSize(pagedTodos.getSize());
		paginatedResponse.setTotalElements(pagedTodos.getTotalElements());
		paginatedResponse.setTotalPages(pagedTodos.getTotalPages());
		paginatedResponse.setLast(pagedTodos.isLast());
		
		return paginatedResponse;
	}
	
	private Todo mapToEntity(TodoDto dto) {
		Todo todo = mapper.map(dto, Todo.class);
		return todo;
	}
	
	private TodoDto mapToDto(Todo todo) {
		TodoDto todoDto = mapper.map(todo, TodoDto.class);
		return todoDto;
	}
	
}
