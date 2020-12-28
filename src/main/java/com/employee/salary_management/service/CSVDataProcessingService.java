package com.employee.salary_management.service;

import java.io.InputStream;

public interface CSVDataProcessingService {

	int saveAllEmployees(InputStream inputStream);
}
