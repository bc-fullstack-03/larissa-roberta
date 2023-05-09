package com.sysmap.parrot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sysmap.parrot.dto.request.AuthenticationRequest;
import com.sysmap.parrot.dto.response.AuthenticationResponse;
import com.sysmap.parrot.service.IAuthenticationService;

@RestController
@RequestMapping("/api/v1/authentication")
public class AuthenticationController {
	@Autowired
	private IAuthenticationService service;

	@PostMapping
	public ResponseEntity<AuthenticationResponse> authentication(@RequestBody AuthenticationRequest request) {
		return ResponseEntity.ok().body(service.authentication(request));
	}
}
