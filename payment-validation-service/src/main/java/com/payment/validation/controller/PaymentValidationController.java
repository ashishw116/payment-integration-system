package com.payment.validation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.payment.validation.model.request.PaymentRequest;
import com.payment.validation.model.response.PaymentResponse;
import com.payment.validation.service.PaymentValidationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/payment")
@Slf4j
@AllArgsConstructor
@Tag(name="Payment Validation" , description = "Payment Validation APIs")
public class PaymentValidationController {
	private final PaymentValidationService validationService;
	
	@PostMapping("/validate")
	@Operation(summary = "Validation payment request", description = "Validates payment request fields and business rules")
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "200", description = "Validation Successful"),
					@ApiResponse(responseCode = "400", description = "Validation Failed")
			})
	public ResponseEntity<PaymentResponse> validatePayment(@Valid @RequestBody PaymentRequest request)
	{
		log.info("Received payment validation request for orderId: {}",request.getOrderId());
		PaymentResponse response=validationService.validatePayment(request);
		log.info("Validation result : {}",response.getStatus());
		return ResponseEntity.ok(response);
	}
}
