package com.payment.processing.model.request;

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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentProcessRequest {
	@NotNull(message = "Amount Required")
	@Positive(message = "Amount Should Be Positive")
	private BigDecimal amount;
	@NotBlank(message = "Currency Required")
	private String currency;
	@NotBlank(message = "Customer ID required")
	private String customerId;
	@NotBlank(message = "Customer name required")
	private String customerName;
	@NotBlank(message = "Customer Email Required")
	@Email(message = "Required Valid Email")
	private String customerEmail;
	@NotBlank(message = "Payment Method Required")
	private String paymentMethod;
	@NotBlank(message = "Order Id Required")
	private String orderId;
	private String description;
	@NotBlank(message = "Merchant Account Required")
	private String merchantId;
	
}
