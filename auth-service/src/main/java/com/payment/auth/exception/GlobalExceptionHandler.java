package com.payment.auth.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.payment.auth.model.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex)
	{
		Map<String,String> errors=new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(),error.getDefaultMessage()));
		log.warn("Validation Failed: {}",errors);
		ErrorResponse response=ErrorResponse.builder()
				.status("FAILED")
                .message("Validation Failed")
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
		return ResponseEntity.badRequest().body(response);
	}
	@ExceptionHandler(AuthException.class)
	public ResponseEntity<ErrorResponse> handleAuthException(AuthException ex)
	{
		log.error("Auth Exception: {}",ex.getMessage());
		ErrorResponse response=ErrorResponse.builder()
				.status("FAILED")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex)
	{
		log.error("Unexpected error: {}",ex.getMessage());
		ErrorResponse response=ErrorResponse.builder()
				.status("FAILED")
                .message("Internal Server Error")
                .timestamp(LocalDateTime.now())
                .build();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
}
