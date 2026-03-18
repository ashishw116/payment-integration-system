package com.payment.validation.model.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
	@NotBlank(message = "Currency must valid")
	@Size(min=3,max=3,message = "Currency must be 3 Charecters")
	private String currency;
	@NotBlank(message = "Customer Id is required")
	private String customerId;
	@NotBlank(message = "Customer name is required")
	private String customerName;
	@NotBlank(message = "Customer email is required")
	@Email(message = "Email must be valid")
	private String customerEmail;
	@NotBlank(message = "Payment method is required")
	private String paymentMethod;
	private String description;
	@NotBlank(message = "Order Id is required")
	private String orderId;
}
