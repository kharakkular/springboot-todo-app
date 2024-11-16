package com.springboot.todo.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "todos", uniqueConstraints = { @UniqueConstraint(columnNames = { "task" })})
public class Todo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "completed", nullable = false)
	private boolean completed;
	
	@Column(name = "task", nullable = false)
	private String task;
	
	@Column(name = "createdOn", nullable = false)
	private Date creationDate;

	public Todo(Long id, boolean completed, String task, Date creationDate) {
		super();
		this.id = id;
		this.completed = completed;
		this.task = task;
		this.creationDate = creationDate;
	}
	
	public Todo() { }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public String toString() {
		return "Todo: " + this.task;
	}
}
