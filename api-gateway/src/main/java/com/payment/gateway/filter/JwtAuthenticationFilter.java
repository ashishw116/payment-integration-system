package com.payment.gateway.filter;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class JwtAuthenticationFilter implements GlobalFilter, Ordered{
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("#{'${gateway.public-endpoints}'.split(',')}")
	private List<String> PUBLIC_ENDPOINTS;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String path=exchange.getRequest()
				.getURI().getPath();
		log.info("Gateway filter processing : {}",path);
		if(isPublicEndpoint(path))
		{
			log.info("Public endpoint - skipping auth: {}",path);
			return chain.filter(exchange);
		}
		String authHeader=exchange.getRequest()
				.getHeaders()
				.getFirst(HttpHeaders.AUTHORIZATION);
		if(authHeader==null||!authHeader.startsWith("Bearer "))
		{
			log.error("Missing or invalid " +
                    "Authorization header for: {}", path);
            return unauthorizedResponse(exchange);
		}
		String token=authHeader.substring(7);
		try
		{
			Claims claims=Jwts.parserBuilder()
					.setSigningKey(Keys.hmacShaKeyFor(
							secret.getBytes()))
					.build()
					.parseClaimsJws(token)
					.getBody();
			String merchantId = claims.get("merchantId", String.class);
            String email = claims.getSubject();
            String role = claims.get("role", String.class);

            log.info("JWT valid for merchantId: {}, " +
                    "email: {}", merchantId, email);
            
            ServerWebExchange mutatedExchange=exchange.mutate()
            		.request(exchange.getRequest()
            				.mutate()
            				.header("X-Merchant-Id", 
                                    merchantId)
                            .header("X-Merchant-Email", 
                                    email)
                            .header("X-Merchant-Role", 
                                    role)
            				.build())
            		.build();
            return chain.filter(mutatedExchange);
		}
		catch (Exception e) {
			log.error("JWT validation failed: {}", 
                    e.getMessage());
            return unauthorizedResponse(exchange);
		}
	}
	
	private Mono<Void> unauthorizedResponse(
            ServerWebExchange exchange) {
        exchange.getResponse()
                .setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
	
	private boolean isPublicEndpoint(String path) {
        return PUBLIC_ENDPOINTS.stream()
                .anyMatch(path::startsWith);
    }
	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

}
