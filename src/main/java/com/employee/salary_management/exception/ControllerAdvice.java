package com.employee.salary_management.exception;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

	@ExceptionHandler(ApiException.class)
	public final ResponseEntity<ErrorResponse> handleAllExceptions(ApiException ex, WebRequest request) {

		List<String> details = new ArrayList<String>();
		details.add(ex.getLocalizedMessage());
		ErrorResponse error = new ErrorResponse(ex.getMessage(), details);
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ InvalidFormatException.class, MismatchedInputException.class })
	public ResponseEntity<ErrorResponse> handlerIllegalArgumentException(JsonProcessingException exception,
			ServletWebRequest webRequest) throws IOException {
		List<String> details = new ArrayList<String>();

		ErrorResponse error = new ErrorResponse(exception.getMessage(), details);

		if (exception instanceof InvalidFormatException) {
			for (JsonMappingException.Reference r : ((InvalidFormatException) exception).getPath()) {
				details.add(r.getFieldName() + " is in invalid Format");
				error = new ErrorResponse("Invalid " + r.getFieldName(), details);
			}
		} else {
			details.add(exception.getLocalizedMessage());
		}

		return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);

	}
}
