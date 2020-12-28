package com.employee.salary_management.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.employee.salary_management.model.Employee;

@Repository
public interface EmployeeRepository extends PagingAndSortingRepository<Employee, String> {

	Optional<Employee> findOneByLogin(String login);

}
