package com.payment.stripe.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.payment.stripe.client.PaymentProcessingClient;
import com.payment.stripe.config.StripeConfig;
import com.payment.stripe.constants.StripeConstants;
import com.payment.stripe.exception.StripeServiceException;
import com.payment.stripe.model.TransactionStatus;
import com.payment.stripe.model.request.StripeCheckoutRequest;
import com.payment.stripe.model.response.StripeCheckoutResponse;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.RefundCreateParams;
import com.stripe.param.checkout.SessionCreateParams;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class StripeServiceImpl implements StripeService {

	private final StripeConfig stripeConfig;
	private final PaymentProcessingClient paymentProcessingClient;

	@Override
	public StripeCheckoutResponse createCheckoutSession(StripeCheckoutRequest request) {
		log.info("Creating Stripe checkout session for orderId: {}, transactionId: {}", request.getOrderId(), request.getTransactionId());
		try {
			long amountInSmallestUnit = request.getAmount().multiply(new BigDecimal(100)).longValue();
			SessionCreateParams params = SessionCreateParams.builder()
					.setMode(SessionCreateParams.Mode.PAYMENT)
					.setSuccessUrl(
							request.getSuccessUrl() != null ? request.getSuccessUrl() : stripeConfig.getSuccessUrl())
					.setCancelUrl(request.getCancelUrl() != null ? request.getCancelUrl() : stripeConfig.getCancelUrl())
					.setCustomerEmail(request.getCustomerEmail())
					.putMetadata("transactionId", request.getTransactionId())
					.putMetadata("orderId", request.getOrderId())
					.addLineItem(
							SessionCreateParams.LineItem.builder()
									.setQuantity(1L)
									.setPriceData(
											SessionCreateParams.LineItem.PriceData.builder()
													.setCurrency(request.getCurrency().toLowerCase())
													.setUnitAmount(amountInSmallestUnit)
													.setProductData(
															SessionCreateParams.LineItem.PriceData.ProductData.builder()
																	.setName("Order Payment: " + request.getOrderId())
																	.build())
													.build())
									.build())
					.build();
			Session session = Session.create(params);
			log.info("Stripe Checkout Session created successfully. Session ID: {}", session.getId());
			return StripeCheckoutResponse.builder()
					.sessionId(session.getId())
					.checkoutUrl(session.getUrl())
					.status(TransactionStatus.SUCCESS.name())
					.message(StripeConstants.CHECKOUT_CREATED)
					.build();
		} catch (StripeException e) {
			log.error("Stripe API error while creating checkout session: {}", e.getMessage(), e);
			throw new StripeServiceException("Failed to create Stripe checkout session: " + e.getMessage());
		}
	}

	@Override
	public String handleWebhook(String payload, String sigHeader) {
		log.info("Processing incoming Stripe Webhook...");
		if (sigHeader == null || sigHeader.isBlank())
		{
			log.error("Stripe webhook verification failed: Missing Stripe-Signature header");
			throw new StripeServiceException("Missing Stripe-Signature header");
		}
		Event event;
		try {
			event = Webhook.constructEvent(payload, sigHeader, stripeConfig.getWebhookSecret());
			log.info("Webhook constructEvent success. Event ID: {}, Type: {}, API Version: {}", 
					event.getId(), event.getType(), event.getApiVersion());
		} catch (SignatureVerificationException e) {
			log.error("Invalid Stripe webhook signature : {}", e.getMessage());
			throw new StripeServiceException("Invalid Stripe webhook signature: " + e.getMessage());
		} catch (Exception e) {
			log.error("Error parsing webhook payload : {}", e.getMessage());
			throw new StripeServiceException("Failed to process webhook: " + e.getMessage());
		}

		EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
		StripeObject stripeObject = null;
		if (dataObjectDeserializer.getObject().isPresent()) {
			stripeObject = dataObjectDeserializer.getObject().get();
			log.info("Deserialized StripeObject using standard deserializer: {}", stripeObject.getClass().getSimpleName());
		} else {
			log.warn("Stripe API version mismatch (Event API Version: {}). Falling back to deserializeUnsafe()",
					event.getApiVersion());
			try {
				stripeObject = dataObjectDeserializer.deserializeUnsafe();
				if (stripeObject != null) {
					log.info("Successfully deserialized StripeObject via deserializeUnsafe(): {}", stripeObject.getClass().getSimpleName());
				} else {
					log.error("deserializeUnsafe() returned null for event type: {}", event.getType());
				}
			} catch (Exception e) {
				log.error("Failed to deserialize webhook payload using deserializeUnsafe(): {}", e.getMessage(), e);
			}
		}
		if (stripeObject == null) {
			log.error("Webhook processing stopped: Unable to obtain StripeObject from payload for event {}", event.getType());
			return StripeConstants.WEBHOOK_PROCESSED;
		}
		switch (event.getType()) {
			case "checkout.session.completed":
				if (stripeObject instanceof Session session) {
					String transactionId = session.getMetadata() != null ? session.getMetadata().get("transactionId")
							: null;
					String orderId = session.getMetadata() != null ? session.getMetadata().get("orderId") : null;
					log.info("Checkout Session completed for transactionId: {}, orderId: {}, sessionId: {}",
							transactionId, orderId, session.getId());
					if (transactionId != null) {
						log.info("calling update status for transactionId: {}", transactionId);
						updateStatusSafely(transactionId, TransactionStatus.SUCCESS);
					}
					else 
					{
						log.warn("Missing 'transactionId' in metadata for completed checkout session ID: {}", session.getId());
					}
				} 
				else 
				{
					log.error("Expected StripeObject of type Session for event 'checkout.session.completed', but got: {}",
							stripeObject.getClass().getName());
				}
				break;
			case "payment_intent.succeeded":
				if (stripeObject instanceof PaymentIntent paymentIntent) {
					String transactionId = paymentIntent.getMetadata() != null
							? paymentIntent.getMetadata().get("transactionId")
							: null;
					String orderId = paymentIntent.getMetadata() != null ? paymentIntent.getMetadata().get("orderId")
							: null;
					log.info("Payment Intent succeeded for transactionId: {}, orderId: {}, paymentIntentId: {}",
							transactionId, orderId, paymentIntent.getId());
					if (transactionId != null) {
						updateStatusSafely(transactionId, TransactionStatus.SUCCESS);
					} 
					else {
						log.warn("Missing 'transactionId' in metadata for succeeded payment intent ID: {}", paymentIntent.getId());
					}
				} 
				else {
					log.error("Expected StripeObject of type PaymentIntent for event 'payment_intent.succeeded', but got: {}",
							stripeObject.getClass().getName());
				}
				break;
			case "payment_intent.payment_failed":
				if (stripeObject instanceof PaymentIntent paymentIntent) {
					String transactionId = paymentIntent.getMetadata() != null
							? paymentIntent.getMetadata().get("transactionId")
							: null;
					String orderId = paymentIntent.getMetadata() != null ? paymentIntent.getMetadata().get("orderId")
							: null;
					log.info("Payment Intent failed for transactionId: {}, orderId: {}, paymentIntentId: {}",
							transactionId, orderId, paymentIntent.getId());
					if (transactionId != null) {
						updateStatusSafely(transactionId, TransactionStatus.FAILED);
					} 
					else {
						log.warn("Missing 'transactionId' in metadata for failed payment intent ID: {}", paymentIntent.getId());
					}
				} 
				else {
					log.error("Expected StripeObject of type PaymentIntent for event 'payment_intent.payment_failed', but got: {}",
							stripeObject.getClass().getName());
				}
				break;
			case "checkout.session.expired":
				if (stripeObject instanceof Session session) {
					String transactionId = session.getMetadata() != null ? session.getMetadata().get("transactionId")
							: null;
					String orderId = session.getMetadata() != null ? session.getMetadata().get("orderId") : null;
					log.info("Checkout Session Expired for transactionId: {}, orderId: {}, sessionId: {}",
							transactionId, orderId, session.getId());
					if (transactionId != null) {
						updateStatusSafely(transactionId, TransactionStatus.EXPIRED);
					} 
					else {
						log.warn("Missing 'transactionId' in metadata for expired checkout session ID: {}", session.getId());
					}
				} 
				else {
					log.error("Expected StripeObject of type Session for event 'checkout.session.expired', but got: {}",
							stripeObject.getClass().getName());
				}
				break;
			default:
				log.info("Unhandled webhook event type: {}", event.getType());
				break;
		}
		return StripeConstants.WEBHOOK_PROCESSED;
	}

	private void updateStatusSafely(String transactionId, TransactionStatus status) {
		log.info("Calling paymentProcessingClient to update status to {} for transactionId: {}", status, transactionId);
		try {
			paymentProcessingClient.updateTransactionStatus(transactionId, status);
			log.info("Update payment-processing-service status to {} for transactionId: {}", status, transactionId);
		} catch (Exception e) {
			log.error("Failed to update status in payment_processing-service for transactionId {}: {}", transactionId,
					e.getMessage());
		}
	}

	@Override
	public String refundPayment(String paymentIntentId, BigDecimal amount) {
		log.info("Initiating refund for paymentIntentId: {}, amount: {}", paymentIntentId, amount);
		try {
			RefundCreateParams.Builder builder = RefundCreateParams.builder()
					.setPaymentIntent(paymentIntentId);

			if (amount != null) {
				long amountInCents = amount.multiply(new BigDecimal(100)).longValue();
				builder.setAmount(amountInCents);
			}
			Refund refund = Refund.create(builder.build());
			log.info("Refund created successfully: {} status: {}", refund.getId(), refund.getStatus());
			return refund.getStatus();
		} catch (StripeException e) {
			log.error("Error creating refund: {}", e.getMessage(), e);
			throw new StripeServiceException("Failed to refund payment: " + e.getMessage());
		}
	}
}
