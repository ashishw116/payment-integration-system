package com.payment.stripe.service;

import java.math.BigDecimal;

import com.payment.stripe.model.request.StripeCheckoutRequest;
import com.payment.stripe.model.response.StripeCheckoutResponse;

public interface StripeService {
	StripeCheckoutResponse createCheckoutSession(StripeCheckoutRequest request);

	String handleWebhook(String payload, String sigHeader);

	String refundPayment(String paymentIntentId, BigDecimal amount);

}
