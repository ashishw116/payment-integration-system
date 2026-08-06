package com.payment.stripe.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	@ExceptionHandler(StripeServiceException.class)
	ResponseEntity<ErrorResponse> stripeServiceExceptionHandler(StripeServiceException ex)
	{
		log.error("StripeServiceException occurred: {}", ex.getMessage(), ex);
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ErrorResponse.builder()
						.status("UNPROCESSABLE_ENTITY")
						.message(ex.getMessage())
						.timestamp(LocalDateTime.now())
						.build()
				);
	}
	
	@ExceptionHandler(Exception.class)
	ResponseEntity<ErrorResponse> globalExceptionHandler(Exception ex)
	{
		log.error("Unhandled Exception occurred: {}", ex.getMessage(), ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.builder()
						.status("INTERNAL_SERVER_ERROR")
						.message(ex.getMessage())
						.timestamp(LocalDateTime.now())
						.build()
				);
	}
}
