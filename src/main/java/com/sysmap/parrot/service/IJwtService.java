package com.sysmap.parrot.service;

import java.security.Key;
import java.util.UUID;

public interface IJwtService {
	String generateToken(UUID userId);

	Key genSignKey();

	boolean isValidToken(String token, String userId);
}
