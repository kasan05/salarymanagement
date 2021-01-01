package com.employee.salary_management.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import com.employee.salary_management.dto.EmployeeDTO;
import com.employee.salary_management.exception.ApiException;

@Component
public class CsvFileReader {

	public List<EmployeeDTO> getEmployeeList(InputStream is) {
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				CSVParser csvParser = new CSVParser(fileReader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

			List<EmployeeDTO> emploployeeList = new ArrayList<EmployeeDTO>();

			Iterable<CSVRecord> csvRecords = csvParser.getRecords();

			List<String> idList = new ArrayList<String>();
			List<String> loginList = new ArrayList<String>();

			for (CSVRecord csvRecord : csvRecords) {
				String id = csvRecord.get("Id");
				String login = csvRecord.get("login");
				if (idList.contains(id)) {
					throw new ApiException("duplicate row");
				} else {
					idList.add(id);
				}
				if (loginList.contains(login)) {
					throw new ApiException("login should be unique");
				} else {
					loginList.add(login);
				}

				double salary = 0.0;
				try {
					salary = Double.parseDouble(csvRecord.get("salary"));
				} catch (Exception e) {
					throw new ApiException("invalid salary");
				}

				EmployeeDTO employeeDTO = new EmployeeDTO(id, csvRecord.get("login"), csvRecord.get("name"), salary,
						convert(csvRecord.get("startDate")));

				emploployeeList.add(employeeDTO);
			}

			return emploployeeList;
		} catch (Exception e) {
			throw new ApiException("parsing error");
		}
	}

	private LocalDate convert(String date) {
		DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter dateTimeFormatter3 = DateTimeFormatter.ofPattern("dd-MMM-yy");

		LocalDate localDate = null;
		try {
			localDate = LocalDate.parse(date, dateTimeFormatter2);
		} catch (Exception e) {
			try {
				localDate = LocalDate.parse(date, dateTimeFormatter3);
			} catch (Exception e2) {

			}
		}

		return localDate;

	}
}
