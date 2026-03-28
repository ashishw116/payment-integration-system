package com.payment.processing.model.response;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
	private String status;
	private String message;
	private Map<String,String> errors;
	private LocalDateTime timestamp;
}
