package com.employee.salary_management.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.employee.salary_management.constant.ApiError;
import com.employee.salary_management.dto.EmployeeDTO;
import com.employee.salary_management.exception.ApiException;
import com.employee.salary_management.mapper.EmployeeMapper;
import com.employee.salary_management.model.Employee;
import com.employee.salary_management.repository.EmployeeRepository;
import com.employee.salary_management.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private EmployeeMapper employeeMapper;

	@Override
	public EmployeeDTO save(EmployeeDTO employeeDTO) {
		Employee employee = employeeMapper.employeeDTOToEmployee(employeeDTO);
		return employeeMapper.employeeToEmployeeDTO(employeeRepository.save(employee));
	}

	@Override
	public EmployeeDTO findById(String id) throws ApiException {
		return employeeMapper.employeeToEmployeeDTO(
				employeeRepository.findById(id).orElseThrow(() -> new ApiException(ApiError.NO_SUCH_EMPLOYEE)));
	}

	@Override
	public void deleteById(String id) {
		if (employeeRepository.existsById(id)) {
			employeeRepository.deleteById(id);
		} else {
			throw new ApiException(ApiError.NO_SUCH_EMPLOYEE);
		}

	}

	@Override
	public List<EmployeeDTO> findAll(int offset, int limit, double minSalary, double maxSalary, String nameFilter,
			String sortBy) {

		String[] arr = new String[] { "name", "id", "login", "salary" };

		List<Order> orders = new ArrayList<Order>();

		if (Optional.ofNullable(sortBy).isPresent()) {

			orders = Arrays.asList(sortBy.split(",")).stream().filter(name -> Arrays.asList(arr).contains(name))
					.map(name -> new Order(Direction.ASC, name)).collect(Collectors.toList());

		}

		
		PageRequest pageRequest = PageRequest.of(offset, limit);

		if (!orders.isEmpty()) {
			Sort sort = Sort.by(orders);
			pageRequest = PageRequest.of(offset, limit, sort);
		}

		List<Employee> employeeList = employeeRepository.findAll(pageRequest).getContent();
		
		
		
		
		if(Optional.ofNullable(nameFilter).isPresent()) {
			employeeList = employeeList.stream()
					.filter(e -> e.getSalary() < maxSalary && e.getSalary() >= minSalary)
					.filter(e->e.getName().startsWith(nameFilter))
					.collect(Collectors.toList());
		}else {
			employeeList = employeeList.stream()
			.filter(e -> e.getSalary() < maxSalary && e.getSalary() >= minSalary)
			.collect(Collectors.toList());
		}

		return employeeMapper.employeeListToEmployeeDTOList(employeeList);
	}

	@Override
	public EmployeeDTO update(String id, EmployeeDTO employeeDTO) throws ApiException {

		String login = employeeDTO.getLogin();
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ApiException(ApiError.NO_SUCH_EMPLOYEE));

		if (Optional.ofNullable(login).isPresent() && !employee.getLogin().equals(employeeDTO.getLogin())) {
			if (employeeRepository.findOneByLogin(employeeDTO.getLogin()).isPresent()) {
				throw new ApiException(ApiError.LOGIN_NOT_UNIQUE);
			}
		}

		employee.setLogin(login);
		employee.setName(employeeDTO.getName());
		employee.setSalary(employeeDTO.getSalary());
		employee.setStartDate(employeeDTO.getStartDate());
		employee = employeeRepository.save(employee);

		return employeeMapper.employeeToEmployeeDTO(employee);
	}

}
