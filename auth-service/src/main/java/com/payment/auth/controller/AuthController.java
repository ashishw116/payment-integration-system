package com.payment.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.payment.auth.model.request.LoginRequest;
import com.payment.auth.model.request.RegisterRequest;
import com.payment.auth.model.response.AuthResponse;
import com.payment.auth.model.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Merchant Auth APIs")
@Slf4j
public class AuthController {
	private final AuthService service;
	@PostMapping("/register")
	@Operation(
	        summary = "Register new merchant",
	        description = "Register merchant account and get JWT token")
	public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request)
	{
		log.info("REST Request to register merchant: {}",request.getEmail());
		AuthResponse response=service.register(request);
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}
	@PostMapping("/login")
	@Operation(
	        summary = "Merchant login",
	        description = "Login and get JWT token")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request)
	{
		log.info("REST Request to login merchant: {}",request.getEmail());
		AuthResponse response=service.login(request);
		return ResponseEntity.ok(response);
	}
}
