package com.payment.validation.model.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.payment.validation.model.TransactionStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentProcessResponse {
	private String transactionId;
	private TransactionStatus status;
	private String orderId;
	private BigDecimal amount;
	private String currency;
	private String checkoutUrl;
	private String message;
	private LocalDateTime createdAt;
	
	public static PaymentProcessResponse success(String transactionId,String orderId,BigDecimal amount,String currency,String checkoutUrl,String message)
	{
		return PaymentProcessResponse.builder()
				.transactionId(transactionId)
				.status(TransactionStatus.PENDING)
				.orderId(orderId)
				.amount(amount)
				.currency(currency)
				.checkoutUrl(checkoutUrl)
				.message(message)
				.createdAt(LocalDateTime.now())
				.build();
	}
	public static PaymentProcessResponse failed(String transactionId,String message)
	{
		return PaymentProcessResponse.builder()
				.transactionId(transactionId)
				.status(TransactionStatus.FAILED)
				.message(message)
				.createdAt(LocalDateTime.now())
				.build();
	}
}
