package com.payment.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.payment.auth.entity.Merchant;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long>{
	Optional<Merchant> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Merchant> findByMerchantId(
            String merchantId);
}
