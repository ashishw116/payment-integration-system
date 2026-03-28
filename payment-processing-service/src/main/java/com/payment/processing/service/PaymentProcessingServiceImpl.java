package com.payment.processing.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.payment.processing.constants.PaymentConstants;
import com.payment.processing.entity.PaymentTransaction;
import com.payment.processing.exception.PaymentProcessingException;
import com.payment.processing.model.TransactionStatus;
import com.payment.processing.model.request.PaymentProcessRequest;
import com.payment.processing.model.response.PaymentProcessResponse;
import com.payment.processing.repository.PaymentTransactionRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class PaymentProcessingServiceImpl implements PaymentProcessingService{

	private final PaymentTransactionRepository repository;
	
	@Transactional
	@Override
	public PaymentProcessResponse processPayment(PaymentProcessRequest processRequest) {
		log.info("Processing payment for order: {}", processRequest.getOrderId());
		if(repository.existsByOrderId(processRequest.getOrderId()))
		{
			log.error("Payment Processing Aborted : Duplicate order detected : {}", processRequest.getOrderId());
			throw new PaymentProcessingException(PaymentConstants.DUPLICATE_ORDER_ERROR);
		}
		PaymentTransaction transaction=PaymentTransaction.builder()
				.transactionId(UUID.randomUUID().toString())
				.orderId(processRequest.getOrderId())
				.merchantId(processRequest.getMerchantId())
				.customerId(processRequest.getCustomerId())
				.customerEmail(processRequest.getCustomerEmail())
				.amount(processRequest.getAmount())
				.currency(processRequest.getCurrency())
				.paymentMethod(processRequest.getPaymentMethod())
				.description(processRequest.getDescription())
				.status(TransactionStatus.PENDING)
				.build();
		PaymentTransaction savedTransaction=repository.save(transaction);
		log.info("Saved new transaction with ID: {}", savedTransaction.getTransactionId());
		return PaymentProcessResponse.builder()
				.transactionId(savedTransaction.getTransactionId())
				.status(savedTransaction.getStatus())
				.orderId(savedTransaction.getOrderId())
				.amount(savedTransaction.getAmount())
				.currency(savedTransaction.getCurrency())
				.message(PaymentConstants.PAYMENT_PROCESSING_SUCCESS)
				.createdAt(savedTransaction.getCreatedAt())
				.build();
	}

	@Override
	public PaymentProcessResponse getTransactionById(String  transactionId) {
		log.info("Retriving transaction details from DB for transactionId : {} ", transactionId);
		PaymentTransaction transaction=repository.findByTransactionId(transactionId).orElseThrow(()-> {
				log.error("Retriving Transection Failed : Transection Not Found : {} ",transactionId );
				return new PaymentProcessingException(PaymentConstants.TRANSECTION_NOT_FOUND);
			});
		return PaymentProcessResponse.builder()
				.transactionId(transaction.getTransactionId())
				.status(transaction.getStatus())
				.orderId(transaction.getOrderId())
				.amount(transaction.getAmount())
				.currency(transaction.getCurrency())
				.checkoutUrl(transaction.getCheckoutUrl())
				.message(PaymentConstants.PAYMENT_PROCESSING_SUCCESS)
				.createdAt(transaction.getCreatedAt())
				.build();
	}

	@Override
	public Page<PaymentProcessResponse> getTransactionByMerchant(String merchantId, Pageable pageable) {
		log.info("Retriving transactions for merchantId : {} ", merchantId);
		Page<PaymentTransaction> transactions=repository.findByMerchantId(merchantId, pageable);
		log.info("Found {} transactions for merchantId : {} ", transactions.getTotalElements(), merchantId);
		return transactions.map(transaction-> PaymentProcessResponse.builder()
				.transactionId(transaction.getTransactionId())
				.status(transaction.getStatus())
				.orderId(transaction.getOrderId())
				.amount(transaction.getAmount())
				.currency(transaction.getCurrency())
				.checkoutUrl(transaction.getCheckoutUrl())
				.message(PaymentConstants.PAYMENT_PROCESSING_SUCCESS)
				.createdAt(transaction.getCreatedAt())
				.build());
	}
	
	@Transactional
	@Override
	public void updateTransaction(String transactionId, TransactionStatus status) {
		log.info("Updat ing request for {} transaction update status to : {} ", transactionId, status);
		PaymentTransaction transaction=repository.findByTransactionId(transactionId).orElseThrow(()->{ 
			log.error("Updating Transection Status Failed : Transection Not Found : {} ",transactionId );
			return new PaymentProcessingException(PaymentConstants.TRANSECTION_NOT_FOUND);
		});
		transaction.setStatus(status);	
		repository.save(transaction);
		log.info("Successfully updated status to {} for transactionId : {} ",status,transactionId );
	}

}
