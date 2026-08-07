package com.payment.stripe.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.payment.stripe.model.TransactionStatus;

@FeignClient(name="payment-processing-service")
public interface PaymentProcessingClient {
	@PutMapping("/api/v1/processing/{transactionId}/status")
	void updateTransactionStatus(@PathVariable("transactionId") String transactionId,@RequestBody TransactionStatus status);
}
