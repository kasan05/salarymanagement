package com.employee.salary_management.dto;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

@Component
public class EmployeeDTO {

	private String id;

	private String login;

	private String name;

	private double salary;

	private LocalDate startDate;

	public EmployeeDTO() {
	}

	public EmployeeDTO(String id, String login, String name, double salary, LocalDate startDate) {
		super();
		this.id = id;
		this.login = login;
		this.name = name;
		this.salary = salary;
		this.startDate = startDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

}
