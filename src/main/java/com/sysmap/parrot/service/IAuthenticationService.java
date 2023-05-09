package com.sysmap.parrot.service;

import com.sysmap.parrot.dto.request.AuthenticationRequest;
import com.sysmap.parrot.dto.response.AuthenticationResponse;

public interface IAuthenticationService {
	AuthenticationResponse authentication(AuthenticationRequest request);

}
