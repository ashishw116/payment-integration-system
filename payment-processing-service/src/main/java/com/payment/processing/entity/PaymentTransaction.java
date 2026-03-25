package com.payment.processing.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.payment.processing.model.TransactionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentTransaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique=true)
	private String transactionId;
	@Column
	private String orderId;
	@Column
	private String merchantId;
	@Column
	private String customerId;
	@Column
	private String customerEmail;
	@Column(precision = 10, scale = 2)
	private BigDecimal amount;
	@Column(length = 3)
	private String currency;
	@Column
	private String paymentMethod;
	@Column
	@Enumerated(EnumType.STRING)
	private TransactionStatus status;
	@Column
	private String stripeSessionId;
	@Column
	private String checkoutUrl;
	@Column
	private String description;
	@Column(nullable = true)
	private String failureReason;
	@CreationTimestamp
	private LocalDateTime createdAt;
	@UpdateTimestamp
	private LocalDateTime updatedAt;
}
