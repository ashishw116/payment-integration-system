package com.payment.auth.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
	@NotBlank(message = "Merchant name required!!")
	private String name;
	@NotBlank(message = "Merchant email required!!")
	@Email(message = "Required valid email!!")
	private String email;
	@NotBlank(message = "Merchant password required!!")
	@Pattern(
			  regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
			  message = "Password must be at least 8 characters, contain one letter, one number, and one special character"
			)
	private String password;
	@NotBlank(message = "Merchant business name required!!")
	private String businessName;
}
