package com.springboot.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.todo.entity.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long>{

}
