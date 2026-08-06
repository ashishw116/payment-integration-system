package com.payment.stripe.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.stripe.Stripe;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Getter
@NoArgsConstructor
@Slf4j
public class StripeConfig {
	@Value("${stripe.api.key}")
	private String apiKey;
	
	@Value("${stripe.webhook.secret}")
	private String webhookSecret;
	
	@Value("${stripe.success.url}")
	private String successUrl;
	
	@Value("${stripe.cancel.url}")
	private String cancelUrl;
	
	@PostConstruct
	public void init()
	{
		Stripe.apiKey=this.apiKey;
		log.info("Stripe SDK initialized with API key");
	}
}
