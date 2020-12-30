package com.employee.salary_management.constant;

public class ApiResponse<T> {

	private String message;
	private T results;

	public ApiResponse() {
		super();
	}

	public ApiResponse(String message, T results) {
		super();
		this.message = message;
		this.results = results;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getResults() {
		return results;
	}

	public void setResults(T results) {
		this.results = results;
	}

}
