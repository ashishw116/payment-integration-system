package com.payment.validation.service;

import com.payment.validation.model.request.PaymentRequest;
import com.payment.validation.model.response.PaymentProcessResponse;
import com.payment.validation.model.response.PaymentResponse;

public interface PaymentValidationService {
	PaymentResponse validatePayment(
	        PaymentRequest request);
	PaymentProcessResponse initiatePayment(
	        PaymentRequest request);
}
