package com.payment.validation.model.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.payment.validation.model.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PaymentResponse {
	private String paymentId;
	private PaymentStatus status;
	private String message;
	private BigDecimal amount;
	private String currency;
	private String orderId;
	private LocalDateTime timestamp;
	public static PaymentResponse success(String message,BigDecimal amount,String currency,String orderID)
	{
		return PaymentResponse.builder()
				.paymentId(UUID.randomUUID().toString())
				.status(PaymentStatus.SUCCESS)
				.message(message)
				.amount(amount)
				.currency(currency)
				.orderId(orderID)
				.timestamp(LocalDateTime.now())
				.build();
	}
	public static PaymentResponse failure(String message)
	{
		return PaymentResponse.builder()
				.paymentId(UUID.randomUUID().toString())
				.status(PaymentStatus.SUCCESS)
				.message(message)
				.timestamp(LocalDateTime.now())
				.build();
	}
}
