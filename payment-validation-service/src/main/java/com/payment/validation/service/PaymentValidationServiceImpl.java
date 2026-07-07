package com.payment.validation.service;

import org.springframework.stereotype.Service;

import com.payment.validation.client.PaymentProcessingClient;
import com.payment.validation.constants.PaymentConstants;
import com.payment.validation.model.Currency;
import com.payment.validation.model.PaymentMethod;
import com.payment.validation.model.PaymentStatus;
import com.payment.validation.model.TransactionStatus;
import com.payment.validation.model.request.PaymentProcessRequest;
import com.payment.validation.model.request.PaymentRequest;
import com.payment.validation.model.response.PaymentProcessResponse;
import com.payment.validation.model.response.PaymentResponse;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class PaymentValidationServiceImpl implements PaymentValidationService{
	private final PaymentProcessingClient processingClient;
	@Override
	public PaymentResponse validatePayment(PaymentRequest request)
	{
		if(request.getCurrency() == null || !Currency.isValid(request.getCurrency().name()))
		{
			return PaymentResponse.failure(PaymentConstants.INVALID_CURRENCY);
		}
		if(request.getPaymentMethod() == null || !PaymentMethod.isValid(request.getPaymentMethod().name()))
		{
			return PaymentResponse.failure(PaymentConstants.INVALID_PAYMENT_METHOD);
		}
		return PaymentResponse.success(PaymentConstants.PAYMENT_VALIDATION_SUCCESS, request.getAmount(), request.getCurrency(),request.getOrderId());
	}

	@Override
	public PaymentProcessResponse initiatePayment(PaymentRequest request) {
		log.info("Initiating payment for orderId: {}", request.getOrderId());
		PaymentResponse validation=validatePayment(request);
		if(validation.getStatus() == 
	            PaymentStatus.FAILED) {
	        return PaymentProcessResponse.builder()
	            .status(TransactionStatus.FAILED)
	            .message(validation.getMessage())
	            .build();
	    }
		PaymentProcessRequest processRequest = 
		        PaymentProcessRequest.builder()
		            .amount(request.getAmount())
		            .currency(request.getCurrency().name())
		            .customerId(request.getCustomerId())
		            .customerName(request.getCustomerName())
		            .customerEmail(request.getCustomerEmail())
		            .paymentMethod(request.getPaymentMethod().name())
		            .orderId(request.getOrderId())
		            .description(request.getDescription())
		            .merchantId("MERCH001") 
		            .build();
		PaymentProcessResponse response=processingClient.processPayment(processRequest);
		log.info("Payment initiated successfully for orderId: {}", request.getOrderId());
		return response;
	}
}