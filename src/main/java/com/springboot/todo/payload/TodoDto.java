package com.springboot.todo.payload;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;

import jakarta.validation.constraints.NotEmpty;

public class TodoDto {
	private long id;
	
	@NotEmpty
	private String task;
	@NotEmpty
	private boolean completed;
	@NotEmpty
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date creationDate;
	
	public TodoDto(long id, @NotEmpty String task, @NotEmpty boolean completed, @NotEmpty Date creationDate) {
		super();
		this.id = id;
		this.task = task;
		this.completed = completed;
		this.creationDate = creationDate;
	}
	
	public TodoDto() { }
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
	}
	public boolean isCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	
}
