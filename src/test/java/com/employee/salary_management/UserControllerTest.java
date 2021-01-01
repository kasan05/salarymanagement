package com.employee.salary_management;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.employee.salary_management.constant.ApiMessage;
import com.employee.salary_management.controller.UserController;
import com.employee.salary_management.dto.EmployeeDTO;
import com.employee.salary_management.mapper.EmployeeMapper;
import com.employee.salary_management.service.CSVDataProcessingService;
import com.employee.salary_management.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

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

	private EmployeeDTO employeeDTO;
	private static final String id = "e003";

	@BeforeEach
	public void preapreTest() {
		employeeDTO = new EmployeeDTO();

		employeeDTO.setId(id);
	}

	@Test
	public void testUpload() throws Exception {

		Resource resource = new ClassPathResource("employeedata.csv");

		MockMultipartFile file = new MockMultipartFile("file", "employeedata.csv", "text/csv",
				resource.getInputStream());

		this.mockMvc.perform(multipart("/users/upload").file(file).accept("text/csv")).andExpect(status().isOk());

	}

	@Test
	public void testGet() throws Exception {
		this.mockMvc.perform(get("/users")).andExpect(status().isOk());
	}

	@Test
	public void testGetUsersById() throws Exception {
		this.mockMvc.perform(get("/users/" + id)).andExpect(status().isOk());
	}

	@Test
	public void testPost() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		this.mockMvc
				.perform(post("/users").contentType("application/json")
						.content(objectMapper.writeValueAsBytes(employeeDTO)))
				.andExpect(status().isCreated()).andExpect(jsonPath("message", is(ApiMessage.CREATION_SUCCESS)));
	}

	@Test
	public void testDeleteUserById() throws Exception {
		this.mockMvc.perform(delete("/users/" + id)).andExpect(status().isOk())
				.andExpect(jsonPath("message", is(ApiMessage.DELETION_SUCCESS)));
	}

	@Test
	public void testupdateUser() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		this.mockMvc
				.perform(put("/users/" + id).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsBytes(employeeDTO)))
				.andExpect(status().isOk()).andExpect(jsonPath("message", is(ApiMessage.UPDATE_SUCCESS)));
	}

}
