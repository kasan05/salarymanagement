package com.employee.salary_management.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.employee.salary_management.dto.EmployeeDTO;
import com.employee.salary_management.exception.ApiException;
import com.employee.salary_management.mapper.EmployeeMapper;
import com.employee.salary_management.service.CSVDataProcessingService;
import com.employee.salary_management.service.EmployeeService;
import com.employee.salary_management.util.CsvFileReader;

@Service
public class CSVDataProcessingServiceImpl implements CSVDataProcessingService {

	@Autowired
	EmployeeService employeeService;

	@Autowired
	CsvFileReader csvFileReader;

	@Autowired
	EmployeeMapper employeeMapper;

	@Override
	public int saveAllEmployees(InputStream inputStream) throws ApiException {

		List<EmployeeDTO> employeeDTOs = csvFileReader.getEmployeeList(inputStream);

		List<EmployeeDTO> employeeDTOList = new ArrayList<EmployeeDTO>();
		employeeDTOs.forEach(e -> {

			EmployeeDTO employeeDTO = employeeService.save(e);
			employeeDTOList.add(employeeDTO);

		});

		return employeeDTOList.size();

	}

}
