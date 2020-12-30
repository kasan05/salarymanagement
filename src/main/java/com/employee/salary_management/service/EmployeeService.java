package com.employee.salary_management.service;

import java.util.List;

import com.employee.salary_management.dto.EmployeeDTO;
import com.employee.salary_management.exception.ApiException;

public interface EmployeeService {

	public EmployeeDTO findById(String id) throws ApiException;

	public EmployeeDTO save(EmployeeDTO employeeDTO) throws ApiException;

	public List<EmployeeDTO> findAll(int offset, int limit, double minSalary, double maxSalary, String nameFilter,
			String sortBy);

	public void deleteById(String id);

	public EmployeeDTO update(String id, EmployeeDTO employeeDTO) throws ApiException;;

}
