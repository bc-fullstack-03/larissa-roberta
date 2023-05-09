package com.sysmap.parrot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sysmap.parrot.dto.request.CommentRequest;
import com.sysmap.parrot.dto.request.PostRequest;
import com.sysmap.parrot.model.Post;
import com.sysmap.parrot.service.IPostService;
import com.sysmap.parrot.service.IUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {

	@Autowired
	private IPostService service;
	@Autowired
	private IUserService userService;

	@PostMapping("/create")
	@Operation(summary = "Create a new post from user", security = @SecurityRequirement(name = "token"))
	public ResponseEntity<Post> createPost(@RequestBody PostRequest request) {
		var post = service.createPost(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(post);
	}

	@GetMapping("/user")
	@Operation(summary = "Get all posts by user", security = @SecurityRequirement(name = "token"))
	public ResponseEntity<List<Post>> findAllByUserId(String userId) {
		return ResponseEntity.ok().body(service.findAllByUserId(userId));
	}

	@PostMapping(value = "/uploadPhoto", name = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "Upload photo post", security = @SecurityRequirement(name = "token"))
	public ResponseEntity<Void> uploadPhoto(@RequestParam(name = "photo", required = false) MultipartFile photo, String postId) {
		service.uploadPhotoPost(photo, postId);
		return new ResponseEntity(HttpStatus.OK);
	}

	@PutMapping("/update")
	@Operation(summary = "update a post", security = @SecurityRequirement(name = "token"))
	public ResponseEntity<Post> updatePost(String postId, @RequestBody PostRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(service.updatePost(postId, request));
	}

	@PostMapping("/like")
	@Operation(summary = "Like/unlike a post", security = @SecurityRequirement(name = "token"))
	public ResponseEntity<String> likePost(String postId, String userId) {
		return ResponseEntity.status(HttpStatus.OK).body(service.likePost(postId, userId));
	}

	@PostMapping("/comment")
	@Operation(summary = "New comment", security = @SecurityRequirement(name = "token"))
	public ResponseEntity<Post> newComment(String postId, @RequestBody CommentRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(service.newComment(postId, request));
	}

	@PutMapping("/comment/update")
	@Operation(summary = "Update a comment", security = @SecurityRequirement(name = "token"))
	public ResponseEntity<Post> updateComment(String postId, String commentId, @RequestBody CommentRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(service.updateComment(postId, commentId, request));
	}

	@GetMapping
	@Operation(summary = "Get all posts", security = @SecurityRequirement(name = "token"))
	public ResponseEntity<List<Post>> findAllPosts() {
		return ResponseEntity.ok().body(service.findAllPosts());
	}

	@DeleteMapping
	@Operation(summary = "Delete a post", security = @SecurityRequirement(name = "token"))
	public ResponseEntity<Void> deletePost(String postId) {
		service.deletePost(postId);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/comment/delete")
	@Operation(summary = "Delete a comment", security = @SecurityRequirement(name = "token"))
	public ResponseEntity<Post> deleteComment(String postId, String commentId) {
		return ResponseEntity.ok().body(service.deleteComment(postId, commentId));
	}

	@PostMapping("/comment/like")
	@Operation(summary = "Like a Comment", security = @SecurityRequirement(name = "token"))
	public ResponseEntity<Post> likeComment(String postId, String commentId) {
		return ResponseEntity.ok().body(service.likeComment(postId, commentId));
	}

	@PostMapping("/comment/unlike")
	@Operation(summary = "Unlike a comment", security = @SecurityRequirement(name = "token"))
	public ResponseEntity<Post> UnlikeComment(String postId, String commentId) {
		return ResponseEntity.ok().body(service.unlikeComment(postId, commentId));
	}

	@GetMapping("/following")
	@Operation(summary = "Get all posts from following List", security = @SecurityRequirement(name = "token"))
	public ResponseEntity<List<Post>> findAllPostsByFollowed() {
		return ResponseEntity.ok().body(service.findAllPostsByFollowed(userService.getAllFollowedId()));
	}
}
