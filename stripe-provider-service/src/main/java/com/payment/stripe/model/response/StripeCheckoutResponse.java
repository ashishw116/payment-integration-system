package com.payment.stripe.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StripeCheckoutResponse {
	private String sessionId;
	private String checkoutUrl;
	private String status;
	private String message;
}
