package com.employee.salary_management.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.employee.salary_management.constant.ApiMessage;
import com.employee.salary_management.constant.ApiResponse;
import com.employee.salary_management.dto.EmployeeDTO;
import com.employee.salary_management.exception.ApiException;
import com.employee.salary_management.mapper.EmployeeMapper;
import com.employee.salary_management.service.CSVDataProcessingService;
import com.employee.salary_management.service.EmployeeService;

@CrossOrigin
@RestController
@RequestMapping(path = "/users")
public class UserController {

	@Autowired
	EmployeeService employeeService;

	@Autowired
	EmployeeMapper employeeMapper;

	@Autowired
	CSVDataProcessingService cSVDataProcessingService;

	@GetMapping
	public ResponseEntity<List<EmployeeDTO>> getUsers(
			@RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
			@RequestParam(name = "limit", required = false, defaultValue = "1") int limit,
			@RequestParam(name = "minSalary", required = false, defaultValue = "0") double minSalary,
			@RequestParam(name = "maxSalary", required = false, defaultValue = "4000") double maxSalary,
			@RequestParam(name = "nameFilter", required = false) String nameFilter,
			@RequestParam(name = "sortBy", required = false) String sortBy) {

		List<EmployeeDTO> employeeDTOs = employeeService.findAll(offset, limit, minSalary, maxSalary, nameFilter,
				sortBy);
		return new ResponseEntity<List<EmployeeDTO>>(employeeDTOs, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<EmployeeDTO> getUsersById(@PathVariable("id") String id) {

		EmployeeDTO employeeDTO = employeeService.findById(id);
		return new ResponseEntity<EmployeeDTO>(employeeDTO, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ApiResponse<EmployeeDTO>> createUser(@RequestBody EmployeeDTO employeeDTO)
			throws ApiException {

		ApiResponse<EmployeeDTO> apiResponse = new ApiResponse<EmployeeDTO>(ApiMessage.CREATION_SUCCESS,
				employeeService.save(employeeDTO));
		return new ResponseEntity<ApiResponse<EmployeeDTO>>(apiResponse, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteUserById(@PathVariable("id") String id) {

		ApiResponse<Void> apiResponse = new ApiResponse<Void>();
		apiResponse.setMessage(ApiMessage.DELETION_SUCCESS);
		employeeService.deleteById(id);
		return new ResponseEntity<ApiResponse<Void>>(apiResponse, HttpStatus.OK);

	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<EmployeeDTO>> updateUser(@PathVariable("id") String id,
			@RequestBody EmployeeDTO employeeDTO) {
		EmployeeDTO employeeDTO2 = employeeService.update(id, employeeDTO);
		ApiResponse<EmployeeDTO> apiResponse = new ApiResponse<EmployeeDTO>();
		apiResponse.setMessage(ApiMessage.UPDATE_SUCCESS);
		apiResponse.setResults(employeeDTO2);
		return new ResponseEntity<ApiResponse<EmployeeDTO>>(apiResponse, HttpStatus.OK);
	}

	@PostMapping("/upload")
	public ResponseEntity<Void> upload(@RequestParam("file") MultipartFile file) throws ApiException {
		int noOfRecords = 0;
		try {
			noOfRecords = cSVDataProcessingService.saveAllEmployees(file.getInputStream());
		} catch (IOException e) {
			throw new ApiException("parsing error");
		}
		if (noOfRecords == 0) {
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.CREATED);
		}

	}

}
