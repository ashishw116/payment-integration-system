package com.payment.processing.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.payment.processing.model.TransactionStatus;
import com.payment.processing.model.request.PaymentProcessRequest;
import com.payment.processing.model.response.PaymentProcessResponse;
import com.payment.processing.service.PaymentProcessingService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/processing")
@Slf4j
@AllArgsConstructor
@Tag(name="Payment Processing")
public class PaymentProcessingController {
	private final PaymentProcessingService processingService;
	
	@PostMapping("/process")
	public ResponseEntity<PaymentProcessResponse>  processPayment(@Valid @RequestBody PaymentProcessRequest processRequest)
	{
		log.info("REST request received to process payment for orderId: {}", processRequest.getOrderId());
		PaymentProcessResponse response=processingService.processPayment(processRequest);
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}
	
	@GetMapping("/{transactionId}")
	public ResponseEntity<PaymentProcessResponse> getTransaction(@PathVariable String transactionId)
	{
		log.info("REST request received to fetch transactionId: {}", transactionId);
		PaymentProcessResponse response=processingService.getTransactionById(transactionId);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/merchant/{merchantId}")
	public ResponseEntity<Page<PaymentProcessResponse>> getTransactionsByMerchant(@PathVariable String merchantId,@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "20") int size)
	{
		log.info("REST request received to fetch transactions for merchantId: {} (Page: {}, Size: {})", merchantId, page, size);
		Pageable pageable=PageRequest.of(page, size);
		Page<PaymentProcessResponse> response=processingService.getTransactionByMerchant(merchantId, pageable);
		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/{transactionId}/status")
	public ResponseEntity<Void> updateTransactionStatus(@PathVariable String transactionId,@RequestBody TransactionStatus status)
	{
		log.info("REST request received to update transactionId: {} to status: {}", transactionId, status);
		processingService.updateTransaction(transactionId, status);
		return ResponseEntity.ok().build();
	}
}
