package com.payment.auth.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
	@NotBlank(message = "Merchant email required!!")
	@Email(message = "Required valid email!!")
	private String email;
	@NotBlank(message = "Merchant password required!!")
	private String password;
}
