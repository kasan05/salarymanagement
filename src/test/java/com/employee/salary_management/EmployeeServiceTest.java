package com.employee.salary_management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.employee.salary_management.constant.ApiMessage;
import com.employee.salary_management.dto.EmployeeDTO;
import com.employee.salary_management.exception.ApiException;
import com.employee.salary_management.mapper.EmployeeMapper;
import com.employee.salary_management.model.Employee;
import com.employee.salary_management.repository.EmployeeRepository;
import com.employee.salary_management.service.EmployeeService;
import com.employee.salary_management.service.impl.EmployeeServiceImpl;

@SpringBootTest
public class EmployeeServiceTest {

	@Mock
	private EmployeeRepository employeeRepository;

	@Mock
	private EmployeeMapper employeeMapper;

	@InjectMocks
	private EmployeeService employeeService = new EmployeeServiceImpl();

	private EmployeeDTO employeeDTO;

	private Employee employee;

	private static final String id = "e003";

	@BeforeEach
	public void preapreTest() {
		employeeDTO = new EmployeeDTO();
		employee = new Employee();
		employee.setId(id);
		employeeDTO.setId(id);
		employee.setLogin("login");
		employeeDTO.setLogin("logina");
	}

	@Test
	public void testSave() {

		when(employeeMapper.employeeDTOToEmployee(employeeDTO)).thenReturn(employee);
		when(employeeRepository.save(employee)).thenReturn(employee);
		when(employeeMapper.employeeToEmployeeDTO(employee)).thenReturn(employeeDTO);

		EmployeeDTO employeeDTO2 = employeeService.save(employeeDTO);

		assertEquals(id, employeeDTO2.getId());

	}

	@Test
	public void testFindById() {
		ApiException apiException = new ApiException(ApiMessage.NO_SUCH_EMPLOYEE);
		when(employeeRepository.findById(id)).thenThrow(apiException);

		ApiException apiException2 = assertThrows(ApiException.class, () -> {
			employeeService.findById(id);

		});

		assertEquals(apiException.getMessage(), apiException2.getMessage());

	}

	@Test
	public void testFindByIdWhenNoEmployeeExists() {

		when(employeeRepository.findById(id)).thenReturn(Optional.ofNullable(employee));
		when(employeeMapper.employeeToEmployeeDTO(employee)).thenReturn(employeeDTO);

		EmployeeDTO employeeDTO = employeeService.findById(id);

		assertEquals(id, employeeDTO.getId());

	}

	@Test
	public void testupdate() {
		when(employeeRepository.findById(id)).thenReturn(Optional.ofNullable(employee));
		when(employeeRepository.findOneByLogin(employeeDTO.getLogin())).thenReturn(Optional.empty());
		when(employeeRepository.save(employee)).thenReturn(employee);
		when(employeeMapper.employeeToEmployeeDTO(employee)).thenReturn(employeeDTO);

		employeeDTO = employeeService.update(id, employeeDTO);

		assertEquals(id, employeeDTO.getId());
	}

	@Test
	public void testupdateWhenNoEmployeeExists() {
		ApiException apiException = new ApiException(ApiMessage.NO_SUCH_EMPLOYEE);

		when(employeeRepository.findById(id)).thenThrow(apiException);

		ApiException exception = assertThrows(ApiException.class, () -> {
			employeeDTO = employeeService.update(id, employeeDTO);
		});
		assertEquals(apiException.getMessage(), exception.getMessage());
	}

	@Test
	public void testupdateWhenLoginisNotUnique() {
		employeeDTO.setLogin("login");
		employee.setLogin("login");
		ApiException apiException = new ApiException(ApiMessage.LOGIN_NOT_UNIQUE);

		when(employeeRepository.findById(id)).thenReturn(Optional.ofNullable(employee));

		when(employeeRepository.findOneByLogin(employeeDTO.getLogin())).thenReturn(Optional.ofNullable(employee));

		ApiException exception = assertThrows(ApiException.class, () -> {
			employeeDTO = employeeService.update(id, employeeDTO);
		});
		assertEquals(apiException.getMessage(), exception.getMessage());
	}

}
