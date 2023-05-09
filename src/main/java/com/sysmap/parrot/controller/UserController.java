package com.sysmap.parrot.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.sysmap.parrot.dto.request.UserRequest;
import com.sysmap.parrot.dto.response.FollowersResponse;
import com.sysmap.parrot.dto.response.UserResponse;
import com.sysmap.parrot.service.IJwtService;
import com.sysmap.parrot.service.IUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
	@Autowired
	private IUserService service;
	@Autowired
	private IJwtService jwtService;

	@GetMapping()
	@Operation(summary = "Get a User", security = @SecurityRequirement(name = "token"))
	public ResponseEntity<UserResponse> getUserByEmail(String email) {
		return ResponseEntity.ok().body(service.findUserByEmail(email));
	}

	@GetMapping("/follow")
	@Operation(summary = "Get a list of users to follow", security = @SecurityRequirement(name = "token"))
	public ResponseEntity<List<FollowersResponse>> getUsersToFollow() {
		return ResponseEntity.ok().body(service.getUsersToFollow());
	}

	@PostMapping("/create")
	@Operation(summary = "Create a new user")
	public ResponseEntity<UUID> createUser(@RequestBody UserRequest request) {
		UUID user = service.createUser(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(user);
	}

	@PostMapping(value = "/photo/upload", name = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "Upload photo user profile", security = @SecurityRequirement(name = "token"))
	public ResponseEntity<Void> uploadPhotoProfile(@RequestParam("photo") MultipartFile photo) {
		try {
			service.uploadPhotoProfile(photo);
			return new ResponseEntity(HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PostMapping("/add")
	@Operation(summary = "Add follower", security = @SecurityRequirement(name = "token"))
	public ResponseEntity<UserResponse> addFollower(String email) {
		return ResponseEntity.ok().body(service.addFollower(email));
	}

	@DeleteMapping("/delete")
	@Operation(summary = "Delete a user", security = @SecurityRequirement(name = "token"))
	public ResponseEntity<Void> deleteById() {
		service.deleteUser();
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/follower/unfollow")
	@Operation(summary = "Unfollow a follower", security = @SecurityRequirement(name = "token"))
	public ResponseEntity<UserResponse> unfollow(String email) {
		return ResponseEntity.ok().body(service.unfollow(email));
	}

	@PutMapping("/update")
	@Operation(summary = "Update user", security = @SecurityRequirement(name = "token"))
	public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest request) {
		var user = new UserResponse(service.findUserById(service.updateUser(request).getId()));
		return ResponseEntity.ok().body(user);
	}
}
