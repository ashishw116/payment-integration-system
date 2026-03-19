package com.payment.validation.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.payment.validation.model.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationExcption(MethodArgumentNotValidException ex)
	{
		log.warn("Validation failed : {}",ex.getMessage());
		Map<String,String> errors=new HashMap<>();
		ex.getBindingResult()
			.getFieldErrors()
			.forEach( error -> 
					errors.put(error.getField(), error.getDefaultMessage()));
		ErrorResponse response=ErrorResponse.builder()
				.status("FAILED")
				.message("Validation Failed")
				.errors(errors)
				.timestamp(LocalDateTime.now())
				.build();
		return ResponseEntity.badRequest().body(response);
	}
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex)
	{
		log.error("Runtime Exception : {}",ex.getMessage());
		ErrorResponse response=ErrorResponse.builder()
				.status("FAILED")
				.message(ex.getMessage())
				.timestamp(LocalDateTime.now())
				.build();
		return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex)
	{
		log.error("Unexpected Exception : {}",ex.getMessage());
		ErrorResponse response=ErrorResponse.builder()
				.status("FAILED")
				.message("Internal server error")
				.timestamp(LocalDateTime.now())
				.build();
		return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
