package com.payment.processing.model.response;

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
}
