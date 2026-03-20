package com.payment.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.payment.validation.model.Currency;
import com.payment.validation.model.PaymentMethod;
import com.payment.validation.model.PaymentStatus;
import com.payment.validation.model.request.PaymentRequest;
import com.payment.validation.model.response.PaymentResponse;
import com.payment.validation.service.PaymentValidationServiceImpl;

public class PaymentValidationServiceApplicationTest {
	private PaymentValidationServiceImpl validationService;
	@BeforeEach
	void setup()
	{
		validationService=new PaymentValidationServiceImpl();
	}
	
	@Test
	void validatePayment_validRequest_returnsSuccess()
	{
		PaymentRequest request=PaymentRequest.builder()
				.amount(new BigDecimal("1000"))
				.currency(Currency.INR)
				.customerId("CUST001")
				.customerName("Ashish Wagh")
				.customerEmail("ashishw112@gmail.com")
				.paymentMethod(PaymentMethod.CARD)
				.orderId("ORD001")
				.build();
		PaymentResponse response=validationService.validatePayment(request);
		assertNotNull(response);
		assertEquals(PaymentStatus.SUCCESS, response.getStatus());
		assertEquals("ORD001", response.getOrderId());
	    assertNotNull(response.getPaymentId());
	    assertNotNull(response.getTimestamp());
	}
	@Test
	void validatePayment_invalidCurrency_returnsFailed()
	{
		PaymentRequest request=PaymentRequest.builder()
				.amount(new BigDecimal("1000"))
				.customerId("CUST001")
				.customerName("Ashish Wagh")
				.customerEmail("ashishw112@gmail.com")
				.paymentMethod(PaymentMethod.CARD)
				.orderId("ORD001")
				.build();
		PaymentResponse response=validationService.validatePayment(request);
		assertNotNull(response);
		assertEquals(PaymentStatus.FAILED, response.getStatus());
	}
	@Test
	void validatePayment_validCurrencyToUSD_returnsSuccess()
	{
		PaymentRequest request=PaymentRequest.builder()
				.amount(new BigDecimal("1000"))
				.currency(Currency.USD)
				.customerId("CUST001")
				.customerName("Ashish Wagh")
				.customerEmail("ashishw112@gmail.com")
				.paymentMethod(PaymentMethod.CARD)
				.orderId("ORD001")
				.build();
		PaymentResponse response=validationService.validatePayment(request);
		assertNotNull(response);
		assertEquals(Currency.USD, response.getCurrency());
		assertEquals(PaymentStatus.SUCCESS, response.getStatus());
	}
	
	@Test
	void validatePayment_invalidMethod_returnsFailed()
	{
		PaymentRequest request=PaymentRequest.builder()
				.amount(new BigDecimal("1000"))
				.currency(Currency.USD)
				.customerId("CUST001")
				.customerName("Ashish Wagh")
				.customerEmail("ashishw112@gmail.com")
				.orderId("ORD001")
				.build();
		PaymentResponse response=validationService.validatePayment(request);
		assertNotNull(response);
		assertEquals(PaymentStatus.FAILED, response.getStatus());
	}
	@Test
	void validatePayment_validMethodToCard_returnsSuccess()
	{
		PaymentRequest request=PaymentRequest.builder()
				.amount(new BigDecimal("1000"))
				.currency(Currency.USD)
				.customerId("CUST001")
				.customerName("Ashish Wagh")
				.customerEmail("ashishw112@gmail.com")
				.paymentMethod(PaymentMethod.CARD)
				.orderId("ORD001")
				.build();
		PaymentResponse response=validationService.validatePayment(request);
		assertNotNull(response);
		assertEquals(PaymentStatus.SUCCESS, response.getStatus());
	}
	
}
