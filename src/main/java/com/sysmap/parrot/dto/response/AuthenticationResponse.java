package com.sysmap.parrot.dto.response;

import java.util.UUID;

import lombok.Data;

@Data
public class AuthenticationResponse {
	private UUID userId;
	private String token;
}
