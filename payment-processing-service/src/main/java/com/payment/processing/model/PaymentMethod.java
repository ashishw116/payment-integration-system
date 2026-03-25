package com.payment.processing.model;

public enum PaymentMethod {
	CARD,
	UPI,
	NETBANKING,
	WALLET;
	public static boolean isValid(String value)
	{
		for(PaymentMethod method : values())
		{
			if(method.name().equalsIgnoreCase(value))
				return true;
		}
		return false;
	}
}
