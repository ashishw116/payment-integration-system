package com.payment.processing.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.payment.processing.model.TransactionStatus;
import com.payment.processing.model.request.PaymentProcessRequest;
import com.payment.processing.model.response.PaymentProcessResponse;

public interface PaymentProcessingService {
	PaymentProcessResponse processPayment(PaymentProcessRequest processRequest);
	PaymentProcessResponse getTransactionById(String transactionId);
	Page<PaymentProcessResponse> getTransactionByMerchant(String merchantId,Pageable pageable);
	void updateTransaction(String transactionId,TransactionStatus status);
}
