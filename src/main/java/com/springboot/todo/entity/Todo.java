package com.springboot.todo.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	
	@Column(name = "createdBy", nullable = false)
	private String createdBy;

	public Todo(Long id, boolean completed, String task, Date creationDate, String createdBy) {
		super();
		this.id = id;
		this.completed = completed;
		this.task = task;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
	}
	
	public Todo() { }
	
	public String getCreatedBy() {
		return this.createdBy;
	}
	
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

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
