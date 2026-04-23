package com.payment.processing.model.request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StripeCheckoutRequest {
	private String transactionId;
	private BigDecimal amount;
	private String currency;
	private String customerEmail;
	private String orderId;
	private String successUrl;
	private String cancelUrl;
}
