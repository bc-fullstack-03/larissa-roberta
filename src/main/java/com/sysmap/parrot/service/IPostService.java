package com.sysmap.parrot.service;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.sysmap.parrot.dto.request.CommentRequest;
import com.sysmap.parrot.dto.request.PostRequest;
import com.sysmap.parrot.model.Post;

public interface IPostService {
	Post createPost(PostRequest request);

	List<Post> findAllPosts();

	Post findPost(String postId);

	List<Post> findAllByUserId(String userId);

	Post updatePost(String postId, PostRequest request);

	List<Post> findAllPostsByFollowed(List<UUID> followersId);

	String likePost(String postId, String userId);

	void deleteAllUserPostsAndLikes(UUID userId);

	void deletePost(String postId);

	Post newComment(String postId, CommentRequest comment);

	Post updateComment(String postId, String commentId, CommentRequest updateC);

	Post deleteComment(String postId, String commentId);

	Post likeComment(String postId, String commentId);

	Post unlikeComment(String postId, String commentId);

	void uploadPhotoPost(MultipartFile photo, String postId);
}
