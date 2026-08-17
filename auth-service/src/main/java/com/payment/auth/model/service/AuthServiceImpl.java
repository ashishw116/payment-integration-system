package com.payment.auth.model.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.payment.auth.constants.AuthConstants;
import com.payment.auth.entity.Merchant;
import com.payment.auth.exception.AuthException;
import com.payment.auth.model.request.LoginRequest;
import com.payment.auth.model.request.RegisterRequest;
import com.payment.auth.model.response.AuthResponse;
import com.payment.auth.repository.MerchantRepository;
import com.payment.auth.security.JwtService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{
	private final MerchantRepository repository;
	private final JwtService jwtService;
	private final PasswordEncoder passwordEncoder;
	@Override
	public AuthResponse register(RegisterRequest request) {
		log.info("Registering merchant with email: {}",request.getEmail());
		if(repository.existsByEmail(request.getEmail()))
		{
			log.error("Email already exists: {}",request.getEmail());
			throw new AuthException(AuthConstants.EMAIL_ALREADY_EXISTS);
		}
		String merchantId="MERCH-"+UUID.randomUUID().toString().substring(0,8).toUpperCase();
		Merchant merchant=Merchant.builder()
				.merchantId(merchantId)
				.name(request.getName())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.businessName(request.getBusinessName())
				.active(true)
				.role("MERCHANT")
				.build();
		Merchant saved=repository.save(merchant);
		log.info("Merchant registered successfully: {}",saved.getMerchantId());
		
		String token=jwtService.generateToken(saved.getEmail(), saved.getMerchantId(),saved.getRole());
				
		return AuthResponse.builder()
				.token(token)
				.merchantId(saved.getMerchantId())
				.email(saved.getEmail())
				.name(saved.getName())
				.tokenType("Bearer")
				.expiresIn(jwtService.getExpiration())
				.build();
	}

	@Override
	public AuthResponse login(LoginRequest request) {
		log.info("Login attempt for email: {}",request.getEmail());
		Merchant merchant=repository.findByEmail(request.getEmail()).orElseThrow(()-> {
			log.error("Merchant not found: {}",request.getEmail());
			return new AuthException(AuthConstants.INVALID_CREDENTIALS);
		});
		if(!passwordEncoder.matches(request.getPassword(), merchant.getPassword()))
		{
			log.error("Invalid password for: {}",request.getEmail());
			throw new AuthException(AuthConstants.INVALID_CREDENTIALS);
		}
		if(!merchant.getActive())
		{
			log.error("Inactive merchant: {}",request.getEmail());
            throw new AuthException("Merchant account is deactivated");
		}
		String token=jwtService.generateToken(merchant.getEmail(), merchant.getMerchantId(), merchant.getRole());
		log.info("Login successful for merchantId: {}",merchant.getMerchantId());
		return AuthResponse.builder()
				.token(token)
				.merchantId(merchant.getMerchantId())
				.email(merchant.getEmail())
				.name(merchant.getName())
				.tokenType("Bearer")
				.expiresIn(jwtService.getExpiration())
				.build();
	}

}
