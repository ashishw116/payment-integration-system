package com.payment.stripe.model.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StripeCheckoutRequest {
	@NotBlank(message = "Transaction ID is required")
	private String transactionId;
	@NotNull(message = "Amount is required")
	@Positive(message = "Amount must be greater then zero")
	private BigDecimal amount;
	@NotBlank(message = "Currency is required")
	private String currency;
	@NotBlank(message = "Customer Email is required")
	@Email(message = "Invalid Email Address")
	private String customerEmail;
	@NotBlank(message = "Order ID is required")
	private String orderId;
	private String successUrl;
	private String cancelUrl;
}
