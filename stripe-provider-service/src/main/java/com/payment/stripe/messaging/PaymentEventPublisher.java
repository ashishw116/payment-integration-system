package com.payment.stripe.messaging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.stripe.constants.StripeConstants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventPublisher {
	private final JmsTemplate jmsTemplate;
	private final ObjectMapper objectMapper;
	@Value("${activemq.queue.payment.notification}")
	private String notificationQueue;
	
	public void publishPaymentSuccess(String transactionId, String orderId, String customerEmail)
	{
		try
		{
			PaymentNotificationEvent event=PaymentNotificationEvent.builder()
					.transactionId(transactionId)
					.orderId(orderId)
					.customerEmail(customerEmail)
					.eventType(StripeConstants.EVENT_PAYMENT_SUCCESS)
					.build();
			String message=objectMapper.writeValueAsString(event);
			jmsTemplate.convertAndSend(notificationQueue,message);
			log.info("Payment success event published for transactionId: {}", transactionId);
		}
		catch (Exception e) {
			log.error("Failed to publish payment event: {}", e.getMessage());
		}
	}
	
	public void publishPaymentFailed(String transactionId, String orderId, String customerEmail)
	{
		try
		{
			PaymentNotificationEvent event=PaymentNotificationEvent.builder()
					.transactionId(transactionId)
					.orderId(orderId)
					.customerEmail(customerEmail)
					.eventType(StripeConstants.EVENT_PAYMENT_FAILED)
					.build();
			String message=objectMapper.writeValueAsString(event);
			jmsTemplate.convertAndSend(notificationQueue,message);
			log.info("Payment failed event published for transactionId: {}", transactionId);
		}
		catch (Exception e) {
			log.error("Failed to publish payment event: {}", e.getMessage());
		}
	}
}
