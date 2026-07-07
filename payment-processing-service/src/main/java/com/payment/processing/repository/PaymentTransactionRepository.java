package com.payment.processing.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.payment.processing.entity.PaymentTransaction;
import com.payment.processing.model.TransactionStatus;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
	Optional<PaymentTransaction> findByTransactionId(String transactionId);
    Optional<PaymentTransaction> findByOrderId(String orderId);
    Page<PaymentTransaction> findByCustomerId(String customerId,Pageable pageable);
    Page<PaymentTransaction> findByMerchantId(String merchantId,Pageable pageable);
    Page<PaymentTransaction> findByStatus(TransactionStatus status,Pageable pageable);
    Page<PaymentTransaction> findByMerchantIdAndStatus(String merchantId, TransactionStatus status,Pageable pageable);
    Boolean existsByOrderId(String orderId);	
}
