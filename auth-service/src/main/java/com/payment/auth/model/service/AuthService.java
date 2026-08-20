package com.payment.auth.model.service;

import com.payment.auth.model.request.LoginRequest;
import com.payment.auth.model.request.RegisterRequest;
import com.payment.auth.model.response.AuthResponse;

public interface AuthService {
	AuthResponse register(RegisterRequest request);
	AuthResponse login(LoginRequest request);
}
