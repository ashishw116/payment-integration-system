package com.payment.stripe.messaging;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.stripe.constants.StripeConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class PaymentEventConsumer {
	private final ObjectMapper objectMapper;
	
	@JmsListener(destination = "${activemq.queue.payment.notification}")
	public void handlePaymentEvent(String message)
	{
		try {
			log.info("Received payment event from queue : {}",message);
			PaymentNotificationEvent event=objectMapper.readValue(message,PaymentNotificationEvent.class);
			log.info("Processing event: {} for transactionId: {}",event.getEventType(),event.getTransactionId());
			if(StripeConstants.EVENT_PAYMENT_SUCCESS.toString().equals(event.getEventType()))
			{
				handlePaymentSuccess(event);
			}
			else if(StripeConstants.EVENT_PAYMENT_FAILED.toString().equals(event.getEventType()))
			{
				handlePaymentFailed(event);
			}
		}
		catch (Exception e) {
			log.error("Error processing payment event: {}", e.getMessage());
		}
	}

	private void handlePaymentFailed(PaymentNotificationEvent event) {
		log.info("❌ PAYMENT FAILED NOTIFICATION:");
        log.info("   TransactionId: {}", 
                event.getTransactionId());
        log.info("   OrderId: {}", 
                event.getOrderId());
        log.info("   → Would send failure " +
                "email to customer!");
	}

	private void handlePaymentSuccess(PaymentNotificationEvent event) {
        log.info("✅ PAYMENT SUCCESS NOTIFICATION:");
        log.info("   TransactionId: {}", 
                event.getTransactionId());
        log.info("   OrderId: {}", 
                event.getOrderId());
        log.info("   Customer Email: {}", 
                event.getCustomerEmail());
        log.info("   → Would send success " +
                "email to customer!");
	}
	
}
