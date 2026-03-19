package com.payment.validation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.payment.validation.model.request.PaymentRequest;
import com.payment.validation.model.response.PaymentResponse;
import com.payment.validation.service.PaymentValidationService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/payment")
@Slf4j
@AllArgsConstructor
public class PaymentValidationController {
	private final PaymentValidationService validationService;
	
	@PostMapping("/validate")
	public ResponseEntity<PaymentResponse> validatePayment(@Valid @RequestBody PaymentRequest request)
	{
		log.info("Received payment validation request for orderId: {}",request.getOrderId());
		PaymentResponse response=validationService.validatePayment(request);
		log.info("Validation result : {}",response.getStatus());
		return ResponseEntity.ok(response);
	}
}
