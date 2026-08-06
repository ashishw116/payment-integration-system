package com.payment.stripe.exception;

public class StripeServiceException extends RuntimeException{
	public StripeServiceException(String message) {
		super(message);
	}
}
