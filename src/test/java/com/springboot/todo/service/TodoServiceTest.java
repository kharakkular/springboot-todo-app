package com.springboot.todo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.springboot.todo.entity.Todo;
import com.springboot.todo.payload.PaginatedResponse;
import com.springboot.todo.payload.TodoDto;
import com.springboot.todo.repository.TodoRepository;

import jakarta.persistence.EntityManager;

@SpringBootTest
public class TodoServiceTest {

	@Mock
	private TodoRepository repository;
	
	@MockBean
	private EntityManager entityManager;
	
	@Mock
	private ModelMapper modelMapper;
	
	@InjectMocks
	private TodoServiceImpl service;
	
	@Test
	public void testGetAllTodos() {
		@SuppressWarnings("deprecation")
		Todo todo1 = new Todo(1L, false, "Task 1", new Date("1/1/2024"));
		Todo todo2 = new Todo(2L, false, "Task 2", new Date("2/1/2024"));
		Todo todo3 = new Todo(3L, false, "Task 3", new Date("3/1/2024"));
		Todo todo4 = new Todo(4L, false, "Task 4", new Date("4/1/2024"));
		Todo todo5 = new Todo(5L, false, "Task 5", new Date("5/1/2024"));
		List<Todo> todos = Arrays.asList(todo1, todo2, todo3, todo4, todo5);
		
		Mockito.when(repository.findByCreationDateBefore(new Date("3/1/2024"), PageRequest.of(0, 2))).thenReturn(new PageImpl<>(todos.stream().filter(todo -> todo.getCreationDate().before(new Date("3/1/2024"))).toList()));
		
		try {
			CompletableFuture<PaginatedResponse<TodoDto>> allTodos = service.getTodosByCreationDateAsync(new Date("3/1/2024"), 0, 2);
			assertEquals(2, (allTodos.get().getNumberOfCurrentPageItems()));
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
