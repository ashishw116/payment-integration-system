package com.payment.validation.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.payment.validation.model.request.PaymentProcessRequest;
import com.payment.validation.model.response.PaymentProcessResponse;

@FeignClient(name="payment-processing-service")
public interface PaymentProcessingClient {
	@PostMapping("/api/processing/process")
	PaymentProcessResponse processPayment(@RequestBody PaymentProcessRequest processRequest);
}
