package com.payment.auth.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtService {
	@Value("${jwt.secret}")
	private String secretKey;
	
	@Value("${jwt.accessTokenExpiry}")
	private long jwtExpiry;
	
	private SecretKey getSecretKey()
	{
		return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
	}
	
	public String generateToken(String email,String merchantId,String role)
	{
		return Jwts.builder()
				.setSubject(email)
				.claim("merchantId",merchantId)
				.claim("role", role)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+jwtExpiry*1000))
				.signWith(getSecretKey(),SignatureAlgorithm.HS256)
				.compact();
	}
	
	private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
	
	public String extractEmail(String token)
	{
		return extractAllClaims(token).getSubject();
	}
	public String extractMerchantId(String token)
	{
		return extractAllClaims(token).get("merchantId",String.class );
	}
	public String extractRole(String token)
	{
		return extractAllClaims(token).get("role",String.class);
	}
	public Long getExpiration()
	{
		return jwtExpiry;
	}
	public boolean isTokenValid(String token)
	{
		try
		{
			Claims claims=extractAllClaims(token);
			return !claims.getExpiration().before(new Date());
			
		}
		catch (Exception e) {
			log.error("Token validation failed: {}",e.getMessage());
			return false;
		}
	}
}

