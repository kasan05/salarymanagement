package com.employee.salary_management.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.employee.salary_management.dto.EmployeeDTO;
import com.employee.salary_management.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

	EmployeeDTO employeeToEmployeeDTO(Employee employee);

	Employee employeeDTOToEmployee(EmployeeDTO employeeDTO);

	List<EmployeeDTO> employeeListToEmployeeDTOList(List<Employee> employeeList);

	List<Employee> employeeDTOListToEmployeeList(List<EmployeeDTO> employeeDTO);

}
