package com.payment.stripe.model;

public enum TransactionStatus {
	SUCCESS,
	FAILED,
	PENDING,
	PROCESSING,
	REFUNDED,
	CANCELLED,
	EXPIRED
}
