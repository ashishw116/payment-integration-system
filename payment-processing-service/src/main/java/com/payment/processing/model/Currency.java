package com.payment.processing.model;

public enum Currency {
	INR,
	USD,
	EUR,
	GBP;
	public static boolean isValid(String value)
	{
		for(Currency c : values())
		{
			if(c.name().equalsIgnoreCase(value))
				return true;
		}
		return false;
	}
}
