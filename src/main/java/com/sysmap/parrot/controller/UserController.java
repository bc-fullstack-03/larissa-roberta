package com.sysmap.parrot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sysmap.parrot.dto.request.UserRequest;
import com.sysmap.parrot.service.IUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
	@Autowired
	private IUserService service;

	@PostMapping
	public ResponseEntity createUser(@RequestBody @Valid UserRequest request) {
		var response = service.createUser(request);
		return ResponseEntity.status(HttpStatus.CREATED).body("");
	}
}
