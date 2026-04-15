package com.payment.validation.model.request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentProcessRequest {
	private BigDecimal amount;
	private String currency;
	private String customerId;
	private String customerName;
	private String customerEmail;
	private String paymentMethod;
	private String orderId;
	private String description;
	private String merchantId;
	
}
