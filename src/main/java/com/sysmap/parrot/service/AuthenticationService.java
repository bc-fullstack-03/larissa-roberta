package com.sysmap.parrot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sysmap.parrot.dto.request.AuthenticationRequest;
import com.sysmap.parrot.dto.response.AuthenticationResponse;

@Service
public class AuthenticationService implements IAuthenticationService {
	@Autowired
	private IUserService userService;
	@Autowired
	private IJwtService jwtService;

	public AuthenticationResponse authentication(AuthenticationRequest request) {
		var user = userService.findUserById(userService.findUserByEmail(request.getEmail()).getId());

		var token = jwtService.generateToken(user.getId());

		var response = new AuthenticationResponse();

		response.setUserId(user.getId());
		response.setToken(token);

		return response;
	}
}
