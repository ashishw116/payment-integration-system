package com.payment.stripe.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentNotificationEvent {
    private String transactionId;
    private String orderId;
    private String customerEmail;
    private String eventType;
}