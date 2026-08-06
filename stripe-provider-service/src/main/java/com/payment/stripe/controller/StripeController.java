package com.payment.stripe.controller;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.payment.stripe.model.request.StripeCheckoutRequest;
import com.payment.stripe.model.response.StripeCheckoutResponse;
import com.payment.stripe.service.StripeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/stripe")
@RequiredArgsConstructor
@Slf4j
public class StripeController {
    private final StripeService stripeService;

    @PostMapping("/checkout")
    public ResponseEntity<StripeCheckoutResponse> createCheckoutSession(
            @Valid @RequestBody StripeCheckoutRequest request) {
    	log.info("REST request to create checkout session for orderId: {}, transactionId: {}", request.getOrderId(), request.getTransactionId());
    	StripeCheckoutResponse response = stripeService.createCheckoutSession(request);
    	log.info("Checkout session created with sessionId: {}", response.getSessionId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
    	log.info("Webhook received from Stripe");
    	String result = stripeService.handleWebhook(payload, sigHeader);
    	log.info("Webhook processing completed: {}", result);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/refund/{paymentIntentId}")
    public ResponseEntity<String> refundPayment(@PathVariable String paymentIntentId,@RequestParam(required = false) BigDecimal amount) {
        log.info("REST request to refund paymentIntentId: {}, amount: {}", paymentIntentId, amount);
        String status = stripeService.refundPayment(paymentIntentId, amount);
        log.info("Refund status for paymentIntentId {}: {}", paymentIntentId, status);
        return ResponseEntity.ok(status);
    }
}
