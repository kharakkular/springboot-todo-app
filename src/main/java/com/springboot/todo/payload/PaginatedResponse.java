package com.springboot.todo.payload;

import java.util.List;

public class PaginatedResponse<T> {
	private List<T> content;
	private int pageNo;
	private int pageSize;
	private boolean isLast;
	private long totalElements;
	private int totalPages;
	
	public PaginatedResponse() {}
	
	public PaginatedResponse(List<T> content, int pageNo, int pageSize, boolean isLast, int totalElements,
			int totalPages) {
		super();
		this.content = content;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.isLast = isLast;
		this.totalElements = totalElements;
		this.totalPages = totalPages;
	}
	public List<T> getContent() {
		return content;
	}
	public void setContent(List<T> content) {
		this.content = content;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public boolean isLast() {
		return isLast;
	}
	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}
	public long getTotalElements() {
		return totalElements;
	}
	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	
	
}