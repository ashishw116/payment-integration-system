package com.payment.validation.service;

import org.springframework.stereotype.Service;

import com.payment.validation.constants.PaymentConstants;
import com.payment.validation.model.Currency;
import com.payment.validation.model.PaymentMethod;
import com.payment.validation.model.request.PaymentRequest;
import com.payment.validation.model.response.PaymentResponse;

@Service
public class PaymentValidationServiceImpl implements PaymentValidationService{
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
}
