package com.payment.auth.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
	private String token;
	private String merchantId;
	private String email;
	private String name;
	private String tokenType;
	private Long expiresIn;
}
