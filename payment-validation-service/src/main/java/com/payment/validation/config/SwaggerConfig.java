package com.payment.validation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI paymentOpenAPI()
	{
		return new OpenAPI()
				.info(new Info()
						.title("Payment Integration API")
						.description("Payment Integration System using Stripe")
						.version("1.0.0")
						.contact(new Contact()
								.name("Ashish Wagh")
								.email("ashish112@gmail.com")));
	}
}
