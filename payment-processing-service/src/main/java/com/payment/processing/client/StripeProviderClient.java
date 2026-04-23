package com.payment.processing.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.payment.processing.model.request.StripeCheckoutRequest;
import com.payment.processing.model.response.StripeCheckoutResponse;
@FeignClient(name="stripe-provider-service")
public interface StripeProviderClient {
	@PostMapping("/api/stripe/checkout")
	StripeCheckoutResponse processStripe(@RequestBody StripeCheckoutRequest request);
}
