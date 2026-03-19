package com.payment.validation.service;

import com.payment.validation.model.request.PaymentRequest;
import com.payment.validation.model.response.PaymentResponse;

public interface PaymentValidationService {
	public PaymentResponse validatePayment(PaymentRequest request);
}
