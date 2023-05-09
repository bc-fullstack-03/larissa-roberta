package com.sysmap.parrot.service;

import java.security.Key;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService implements IJwtService {
	private final long EXPIRATION_TIME = 7200000;
	private final String KEY = "!z%C*F-JaNcRfUjXn2r5u8x/A?D(G+Kb";

	public String generateToken(UUID userId) {
		return Jwts.builder().setSubject(userId.toString()).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)).signWith(genSignKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	public boolean isValidToken(String token, String userId) {
		var sub = getClaim(token, Claims::getSubject);
		var tExpiration = getClaim(token, Claims::getExpiration);

		if (!sub.equals(userId)) {
			return false;
		}

		if (tExpiration.before(new Date(System.currentTimeMillis()))) {
			return false;
		}
		return true;
	}

	private <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
		var claims = Jwts.parserBuilder().setSigningKey(genSignKey()).build().parseClaimsJws(token).getBody();

		return claimsResolver.apply(claims);
	}

	public Key genSignKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(KEY));
	}
}
