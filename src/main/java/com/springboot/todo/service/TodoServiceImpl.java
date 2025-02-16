package com.springboot.todo.service;

import java.security.Permission;
import java.util.Date;
//import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.springboot.todo.entity.Role;
import com.springboot.todo.entity.Todo;
import com.springboot.todo.entity.User;
import com.springboot.todo.exception.ResourceNotFoundException;
import com.springboot.todo.payload.PaginatedResponse;
import com.springboot.todo.payload.TodoDto;
import com.springboot.todo.repository.TodoRepository;
import com.springboot.todo.repository.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Service
public class TodoServiceImpl implements TodoService{
	
	Logger logger = LogManager.getLogger(TodoServiceImpl.class);
	
	@Autowired
	private TodoAclService aclService;
	
	private EntityManager entityManager;
	
	private TodoRepository todoRepository;
	
	private UserRepository userRepository;
	
	private ModelMapper mapper;
	
	public TodoServiceImpl(TodoRepository _todoRepository, ModelMapper _mapper, EntityManager _manager, UserRepository userRepository) {
		todoRepository = _todoRepository;
		mapper = _mapper;
		entityManager = _manager;
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	@PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
	public TodoDto createTodo(TodoDto todoDto,String user) {
		TodoDto postResponseTodoDto = null;
//		try {
//			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Todo todo = mapToEntity(todoDto);
			todo.setCreatedBy(user);
			Todo postTodo = todoRepository.save(todo);
			logger.info("Created Todo temporarily");
			postResponseTodoDto= mapToDto(postTodo);
			logger.info("Assigning permission for created todo");
			assignUserPermission(postTodo, user);
//		} catch (Exception e) {
//			e.getStackTrace();
//			logger.error("________--------------+++++++++++++================Error message from create todo: " + e.getMessage());
//		}
		return postResponseTodoDto;
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
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	public PaginatedResponse<TodoDto> getAllTodos(String user, int pageNo, int pageSize) {
		
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		
		Page<Todo> pagedTodos = null;
		
		Optional<User> fetchedUser = userRepository.findByUsername(user);
		List<String> userRoles = fetchedUser.get().getRoles().stream().map(role -> role.getRole()).toList();
		
		logger.info("1-1-1-1-1-1-1-1 Roles of the user from getAllTodos: " + userRoles.toString());
		
		if(userRoles.contains("ROLE_ADMIN")) {
			pagedTodos = todoRepository.findAll(pageable);
		} else {
			pagedTodos = todoRepository.findByCreatedBy(user, pageable);
			logger.info("000000000000000 ----------- Total todos fetched: " + pagedTodos.getNumberOfElements());
		}
		
		List<Todo> listOfTodos = pagedTodos.getContent();
		
		List<TodoDto> content = listOfTodos.stream().map(todo -> mapToDto(todo)).toList();
		
		PaginatedResponse<TodoDto> paginatedResponse = new PaginatedResponse();
		paginatedResponse.setContent(content);
		paginatedResponse.setPageNo(pagedTodos.getNumber());
		paginatedResponse.setPageSize(pagedTodos.getSize());
		paginatedResponse.setTotalElements(pagedTodos.getTotalElements());
		paginatedResponse.setTotalPages(pagedTodos.getTotalPages());
		paginatedResponse.setLast(pagedTodos.isLast());
		paginatedResponse.setNumberOfCurrentPageItems(pagedTodos.getNumberOfElements());
		return paginatedResponse;
	}
//	@Transactional
//	@Override
//	public PaginatedResponse<TodoDto> getAllTodos(int pageNo, int pageSize) {
//		
//		int limit = (pageNo -1) * pageSize;
//		String sql = "select * from todos order by id LIMIT ?1 OFFSET ?2";
//		Query query = entityManager.createNativeQuery(sql, Todo.class);
//		query.setParameter(1, pageSize);
//		query.setParameter(2, limit);
//		
//		List listOfTodoDto = query.getResultStream().map(todo -> mapToDto((Todo) todo)).toList();
//					
//		long totalElements = (long) entityManager.createQuery("select count(id) from Todo").getSingleResult();
//		int totalPages = (int) Math.ceil((double) totalElements / pageSize);
//		
//		PaginatedResponse<TodoDto> paginatedResponse = new PaginatedResponse();
//		paginatedResponse.setContent(listOfTodoDto);
//		paginatedResponse.setPageNo(pageNo);
//		paginatedResponse.setPageSize(pageSize);
//		paginatedResponse.setTotalElements(totalElements);
//		paginatedResponse.setTotalPages(totalPages);
//		paginatedResponse.setLast(pageNo >= totalPages);
//		paginatedResponse.setNumberOfCurrentPageItems(listOfTodoDto.size());
//		return paginatedResponse;
//	}
	
	@Override
	@Async
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	public CompletableFuture<PaginatedResponse<TodoDto>> getTodosByCreationDateAsync(String user, Date creationDate, int pageNo, int pageSize) throws InterruptedException {
		
		Pageable pageRequest = PageRequest.of(pageNo, pageSize);
		
		Optional<User> username = userRepository.findByUsername(user);
		List<Role> userRoles = username.get().getRoles();
		
		Page<Todo> todosByCreationDate;
		if(userRoles.contains("ROLES_ADMIN")) {
			todosByCreationDate = todoRepository.findByCreationDateBeforeAndCreatedBy(creationDate, user, pageRequest);
		} else {
			todosByCreationDate = todoRepository.findByCreationDateBefore(creationDate, pageRequest);
		}
		
		List<TodoDto> content = todosByCreationDate.getContent().stream().map(todo -> mapToDto(todo)).toList();
		
		PaginatedResponse<TodoDto> paginatedResponse = new PaginatedResponse<TodoDto>(content, todosByCreationDate.getNumber(), todosByCreationDate.getSize(), !todosByCreationDate.isLast(), 
				todosByCreationDate.getTotalElements(), todosByCreationDate.getTotalPages(), todosByCreationDate.getNumberOfElements());
		Thread.sleep(2000L);
		return CompletableFuture.completedFuture(paginatedResponse);
	}
	
	@Override
	@Async
	public Callable<PaginatedResponse<TodoDto>> getAllTodosbyCompletion(String user, boolean completion, int pageNo, int pageSize) {
		
		
		return () -> {
			PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
			Page<Todo> completionRecords = todoRepository.findByCompleted(completion, pageRequest);
			
			List<TodoDto> content = completionRecords.map(todo -> mapToDto(todo)).getContent();
			
			PaginatedResponse<TodoDto> paginatedResponse = new PaginatedResponse<TodoDto>();
			paginatedResponse.setContent(content);
			paginatedResponse.setLast(completionRecords.isLast());
			paginatedResponse.setNumberOfCurrentPageItems(content.size());
			paginatedResponse.setPageNo(completionRecords.getNumber());
			paginatedResponse.setPageSize(pageSize);
			paginatedResponse.setTotalElements(completionRecords.getTotalElements());
			paginatedResponse.setTotalPages(completionRecords.getTotalPages());
			return paginatedResponse;
		};
	}
		
	private Todo mapToEntity(TodoDto dto) {
		Todo todo = mapper.map(dto, Todo.class);
		return todo;
	}
	
	private TodoDto mapToDto(Todo todo) {
		TodoDto todoDto = mapper.map(todo, TodoDto.class);
		return todoDto;
	}

	private void assignUserPermission(Todo todo, String username) {
		aclService.grantReadPermission(todo, username);
	}
}
