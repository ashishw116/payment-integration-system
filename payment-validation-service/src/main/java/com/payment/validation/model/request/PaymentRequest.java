package com.payment.validation.model.request;

import java.math.BigDecimal;

import com.payment.validation.model.Currency;
import com.payment.validation.model.PaymentMethod;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {
	@NotNull(message = "Amount is required")
	@Positive(message = "Amount must be greater than 0")
	private BigDecimal amount;
	@NotNull(message = "Currency is required")
	private Currency currency;
	@NotBlank(message = "Customer Id is required")
	private String customerId;
	@NotBlank(message = "Customer name is required")
	private String customerName;
	@NotBlank(message = "Customer email is required")
	@Email(message = "Email must be valid")
	private String customerEmail;
	@NotNull(message = "Payment method is required")
	private PaymentMethod paymentMethod;
	private String description;
	@NotBlank(message = "Order Id is required")
	private String orderId;
}
