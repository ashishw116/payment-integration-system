package com.payment.processing.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.payment.processing.model.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex)
	{

		Map<String,String> errors=new HashMap<>();
		ex.getBindingResult().getFieldErrors()
		.forEach(error->errors.put(error.getField(), error.getDefaultMessage()));
		ErrorResponse errorResponse=ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST.name())
                .message("Validation Failed")
                .errors(errors)
                .timestamp(LocalDateTime.now())
				.build();
		log.warn("Validation failed for incoming request: {}", errors);
		return ResponseEntity.badRequest().body(errorResponse);
	}  
	
	@ExceptionHandler(PaymentProcessingException.class)
	public ResponseEntity<ErrorResponse> handlePaymentProcessingException(PaymentProcessingException ex)
	{
		log.warn("Payment Processing Business Exception: {}", ex.getMessage());
		ErrorResponse response=ErrorResponse.builder()
				.status("FAILED")
				.message(ex.getMessage())
				.timestamp(LocalDateTime.now())
				.build();
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGlobalxception(Exception ex)
	{
		log.error("Unhandled Server Error occurred: ", ex);
		ErrorResponse response=ErrorResponse.builder()
				.status("FAILED")
				.message("Internal Server Error")
				.timestamp(LocalDateTime.now())
				.build();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
	
}
