package com.employee.salary_management;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.employee.salary_management.controller.UserController;
import com.employee.salary_management.mapper.EmployeeMapper;
import com.employee.salary_management.service.CSVDataProcessingService;
import com.employee.salary_management.service.EmployeeService;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CSVDataProcessingService cSVDataProcessingService;

	@MockBean
	EmployeeService employeeService;

	@MockBean
	EmployeeMapper employeeMapper;

	@Test
	public void testUpload() throws Exception {

		Resource resource = new ClassPathResource("employeedata.csv");

		MockMultipartFile file = new MockMultipartFile("file", "employeedata.csv", "text/csv",
				resource.getInputStream());

		this.mockMvc.perform(multipart("/users/upload").file(file).accept("text/csv")).andExpect(status().isOk());

	}

	public void add() throws Exception {

		this.mockMvc.perform(get("/users")).andExpect(status().isOk());

	}

}
