package com.payment.auth.exception;

public class AuthException extends RuntimeException{

	public static final long serialVersionUID=1L;
	public AuthException(String message)
	{
		super(message);
	}
}
