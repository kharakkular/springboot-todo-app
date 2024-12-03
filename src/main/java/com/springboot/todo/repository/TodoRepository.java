package com.springboot.todo.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.todo.entity.Todo;
import com.springboot.todo.payload.TodoDto;

public interface TodoRepository extends JpaRepository<Todo, Long>{
	Page<Todo> findByCreationDateBefore(Date creationDate, Pageable pageable);
}
